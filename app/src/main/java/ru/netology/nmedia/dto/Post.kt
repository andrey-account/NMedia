package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0L,
    val author: String = "",
    val content: String = "", //Текст сообщения
    val published: String = "", //Дата и время публикации
    val likedByMe: Boolean = false,  //Лайк включен или отключен
    val likeClickCount: Int = 0, //Счётчик лайков
    val shareClickCount: Int = 0, //Счётчик репостов
    val lookClickCount: Int = 0 //Счётчик просмотров
)
//Значения по умолчанию добавлены, чтобы было проще писать тесты
// (не указывать в каждом тесте все значения класса)