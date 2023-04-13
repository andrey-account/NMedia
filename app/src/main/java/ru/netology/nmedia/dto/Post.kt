package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0,
    val author: String,
    val content: String, //Текст сообщения
    val published: String, //Дата и время публикации
    val likedByMe: Boolean, //Лайк включен или отключен
    val likes: Int = 0, //Счётчик лайков
)

