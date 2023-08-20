package ru.netology.nmedia.auth

// Класс данных для представления состояния авторизации
data class AuthState(
    val id: Long = 0,    // Идентификатор пользователя
    val token: String = ""    // Токен авторизации
)