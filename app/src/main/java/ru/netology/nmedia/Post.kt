package ru.netology.nmedia

data class Post(
    val id: Long = 0L,
    val author: String = "",
    val content: String = "", //Текст сообщения
    val published: String = "", //Дата и время публикации
    var likedByMe: Boolean = false,  //Лайк включен или отключен
    var likeClickCount: Int = 0, //Счётчик лайков
    var shareClickCount: Int = 0, //Счётчик репостов
    var lookClickCount: Int = 0 //Счётчик просмотров
)
//Значения по умолчанию добавлены, чтобы было проще писать тесты
// (не указывать в каждом тесте все значения класса)