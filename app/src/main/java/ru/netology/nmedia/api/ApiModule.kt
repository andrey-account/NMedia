package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Singleton

// Устанавливаем модуль в SingletonComponent
@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    // Предоставляем логгирование для сетевых запросов
    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        // Если приложение находится в режиме отладки, устанавливаем уровень логгирования на полное тело запроса и ответа
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Предоставляем экземпляр OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttp(
        logging: HttpLoggingInterceptor,
        appAuth: AppAuth
    ): OkHttpClient = OkHttpClient.Builder()
        // Добавляем логгирование запросов и ответов с использованием предоставленного HttpLoggingInterceptor
        .addInterceptor(logging)
        // Добавляем перехватчик, который добавляет заголовок "Authorization" с токеном авторизации к каждому запросу, если токен доступен
        .addInterceptor { chain ->
            val request = appAuth.state.value.token?.let { token ->
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", token)
                    .build()
            } ?: chain.request()
            chain.proceed(request)
        }
        // Создаем экземпляр OkHttpClient с добавленными перехватчиками
        .build()

    // Предоставляем экземпляр Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
    ) : Retrofit = Retrofit.Builder()
        // Добавляем конвертер GsonConverterFactory для преобразования JSON в объекты
        .addConverterFactory(GsonConverterFactory.create())
        // Устанавливаем OkHttpClient в качестве клиента для сетевых запросов
        .client(client)
        // Устанавливаем базовый URL для всех запросов
        .baseUrl(BASE_URL)
        // Создаем экземпляр Retrofit с добавленными настройками
        .build()

    // Предоставляем экземпляр ApiService
    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ) : ApiService = retrofit.create()
}