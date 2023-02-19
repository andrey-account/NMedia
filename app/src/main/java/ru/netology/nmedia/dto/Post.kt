package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0L,
    val author: String = "",
    val content: String = "", //Текст сообщения
    val published: String = "", //Дата и время публикации
    val likedByMe: Boolean = false,  //Лайк включен или отключен
    val likes: Long = 0L, //Счётчик лайков
    val shares: Long = 0L, //Счётчик репостов
    val looks: Long = 0L, //Счётчик просмотров
    val video: String = "https://www.youtube.com/watch?v=O80turoMNgM" //Для ссылок на видео из YouTube
)
//Значения по умолчанию добавлены, чтобы было проще писать тесты
// (не указывать в каждом тесте все значения класса)