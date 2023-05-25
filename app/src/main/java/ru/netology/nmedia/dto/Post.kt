package ru.netology.nmedia.dto

import android.net.Uri
import java.io.File
data class Post(
    val id: Long = 0,
    val author: String,
    val authorAvatar: String = "",
    val content: String, //Текст сообщения
    val published: String, //Дата и время публикации
    val likedByMe: Boolean, //Лайк включен или отключен
    val likes: Int = 0, //Счётчик лайков
    val attachment: Attachment?, //Вложение, аватарка
    val show: Boolean = true,
)

data class PhotoModel(val uri: Uri? = null, val file: File? = null)

data class Media(val id: String)

data class Attachment(
    val url: String,
    val description: String,
    val type: AttachmentType,
)

enum class AttachmentType {
    IMAGE
}
