package ru.netology.nmedia.auth

import android.content.Context
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.PushToken
import ru.netology.nmedia.error.ApiException
import ru.netology.nmedia.error.NetworkException
import ru.netology.nmedia.error.UnknownException
import ru.netology.nmedia.model.AuthModel
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN_KEY = "TOKEN_KEY"
private const val ID_KEY = "ID_KEY"

// Компонент синглтона, ответственный за аутентификацию
@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    // Создаем объект SharedPreferences с именем "auth" и приватным доступом
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    // Инициализируем переменную для отслеживания состояния авторизации
    private val _authState: MutableStateFlow<AuthModel>

    // Выполняется при создании экземпляра класса
    init {
        // Получаем токен и идентификатор из SharedPreferences
        val token = prefs.getString(TOKEN_KEY, null)
        val id = prefs.getLong(ID_KEY, 0L)

        // Если токен или идентификатор отсутствуют или равны нулю
        if (token == null || id == 0L) {
            // Инициализируем authState пустой моделью AuthModel
            _authState = MutableStateFlow(AuthModel())
            // Очищаем SharedPreferences
            prefs.edit { clear() }
        } else {
            // Инициализируем authState существующей моделью AuthModel
            _authState = MutableStateFlow(AuthModel(id, token))
        }
        // Отправляем токен для получения push-уведомлений
        sendPushToken()
    }

    // Создаем переменную state, которая предоставляет доступ к текущему состоянию авторизации
    val state = _authState.asStateFlow()

    // Метод для установки новых значений идентификатора и токена авторизации
    @Synchronized
    fun setAuth(id: Long, token: String) {
        // Сохраняем новые значения идентификатора и токена в SharedPreferences
        prefs.edit {
            putLong(ID_KEY, id)
            putString(TOKEN_KEY, token)
        }
        // Обновляем состояние авторизации с новыми значениями идентификатора и токена
        _authState.value = AuthModel(id, token)
        // Отправляем токен для получения push-уведомлений
        sendPushToken()
    }

    // Метод для удаления данных авторизации
    @Synchronized
    fun removeAuth() {
        // Обновляем состояние авторизации с пустой моделью AuthModel
        _authState.value = AuthModel()
        // Очищаем SharedPreferences
        prefs.edit { clear() }
        // Отправляем токен для получения push-уведомлений
        sendPushToken()
    }

    // Аннотация для указания, что класс AppAuthEntryPoint является точкой входа в компоненты приложения
    @InstallIn(SingletonComponent::class)
// Аннотация для обозначения, что класс является точкой входа для получения экземпляра ApiService
    @EntryPoint
    interface AppAuthEntryPoint {
        // Метод для получения экземпляра ApiService
        fun getApiService(): ApiService
    }

    // Метод для отправки push-токена на сервер
    fun sendPushToken(token: String? = null) {
        // Запускаем новую корутину на главном потоке
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // Если передан не-null токен, присваиваем его. Иначе присваиваем пустую строку
                token ?: ""
                // Получаем экземпляр класса AppAuthEntryPoint из приложения
                val entryPoint =
                    EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
                // Получаем экземпляр класса ApiService из AppAuthEntryPoint и вызываем метод saveToken
                entryPoint.getApiService().saveToken(
                    PushToken(
                        // Если передан не-null токен, передаем его. Иначе вызываем await() для получения токена от FirebaseMessaging
                        token ?: FirebaseMessaging.getInstance().token.await()
                    )
                )
            } catch (e: Exception) {
                // В случае возникновения исключения, выводим стек трейс ошибки
                e.printStackTrace()
            }
        }
    }

    // Сопрограмма для регистрации с фотографией
    suspend fun registerWithPhoto(login: String, password: String, name: String, file: File) {
        try {
            // Создаем часть запроса с данными файла (имя файла и его содержимое)
            val fileData = MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody()
            )
            // Получаем экземпляр класса AppAuthEntryPoint из приложения
            val entryPoint =
                EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
            // Вызываем метод registerWithPhoto из ApiService и передаем параметры для регистрации
            val response = entryPoint.getApiService().registerWithPhoto(
                login.toRequestBody(),
                password.toRequestBody(),
                name.toRequestBody(),
                fileData
            )
            // Проверяем успешность выполнения запроса
            if (!response.isSuccessful) {
                // Если запрос не успешен, выбрасываем исключение ApiException с кодом и сообщением ошибки
                throw ApiException(response.code(), response.message())
            }
            // Получаем объект ответа из тела ответа
            val body =
                response.body() ?: throw ApiException(response.code(), response.message())
            // Проверяем, не является ли токен пустым, и если не является, устанавливаем авторизацию с полученным идентификатором и токеном
            body.token?.let { setAuth(body.id, it) }
        } catch (e: IOException) {
            // В случае исключения IOException, выбрасываем NetworkException
            throw NetworkException
        } catch (e: ApiException) {
            // В случае исключения ApiException, просто выбрасываем его дальше
            throw e
        } catch (e: Exception) {
            // В случае других исключений, выбрасываем UnknownException
            throw UnknownException
        }
    }
}