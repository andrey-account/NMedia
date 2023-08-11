package ru.netology.nmedia.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint //Для использования аннотации @AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import javax.inject.Inject //Для использования функционала Dependency Injection

@AndroidEntryPoint //Для пометки класса в качестве точки входа для Hilt.
class AppActivity : AppCompatActivity(R.layout.activity_app) {
    @Inject // Помечаем поле firebaseMessaging аннотацией @Inject, чтобы Dagger мог внедрить зависимость FirebaseMessaging в эту переменную
    lateinit var firebaseMessaging: FirebaseMessaging
    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability
    private lateinit var appBarConfiguration: AppBarConfiguration //Для настройки панели приложения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //Переопределяем метод onCreate для создания активности
        FirebaseApp.initializeApp(this)
        // Получаем интент, который запустил активность
        intent?.let {
            if (it.action != Intent.ACTION_SEND) { //Проверяем, является ли действие интента "отправить"
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT) //Получаем текст сообщения из интента
            if (text?.isNotBlank() != true) { //Проверяем, что текст сообщения не является пустым или состоит только из пробелов
                return@let
            }
            intent.removeExtra(Intent.EXTRA_TEXT) //Удаляем текст сообщения из интента
            findNavController(R.id.nav_host_fragment) //Находим NavController и переходим на фрагмент для создания нового поста, передавая в него текст сообщения
                .navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }
        //Проверяем доступность Google Play Services и выводим сообщение, если они недоступны
        checkGoogleApiAvailability()
        //Находим NavController и AppBarConfiguration и настраиваем панель приложения
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
//Регистрируем обратный вызов, чтобы изменять цвет фона панели приложения при переключении на определенный фрагмент
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    v: View,
                    savedInstanceState: Bundle?
                ) {
                    supportActionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                this@AppActivity,
                                if (f is AttachmentFragment) {
                                    R.color.black_overlay
                                } else {
                                    R.color.colorPrimary
                                }
                            )
                        )
                    )
                }
            },
            true,
        )
    }

    private fun checkGoogleApiAvailability() { //Для проверки доступности Google Play Services и получения токена Firebase Cloud Messaging
        with(googleApiAvailability) {
            val code = isGooglePlayServicesAvailable(this@AppActivity) //Проверяет доступность Google Play Services
            if (code == ConnectionResult.SUCCESS) { //Если Google Play Services доступны, прерываем выполнение функции
                return@with
            }
            if (isUserResolvableError(code)) { //Если возникла ошибка, которую можно исправить, показываем диалоговое окно с описанием ошибки
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show() //Если ошибка не может быть исправлена пользователем, показываем сообщение об ошибке
        }
        //Получаем токен Firebase Cloud Messaging и выводим его в консоль
        firebaseMessaging.token.addOnSuccessListener {
            println(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean { //Для обработки нажатия на кнопку "назад" в AppBar
        //Ищем NavController, связанный с NavHostFragment, и связываем его с переменной navController
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp() //вызывает finish() для закрытия Activity
    }
}