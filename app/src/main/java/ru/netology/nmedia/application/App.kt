package ru.netology.nmedia.application

// Аннотация для интеграции Hilt в приложение
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Класс приложения, расширяющий Application
@HiltAndroidApp
class App : Application()