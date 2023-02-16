package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface OnInteractionListener{
    fun onLike(post: Post) {}
    fun onShare(post: Post) {} // Для обработки нажатия кнопки share
    fun onRemove(post: Post) {} //Для удаления поста
    fun onEdit(post: Post) {} //Для редактирования поста
    fun onLook(post: Post) {} // Для обработки нажатия кнопки look
    fun onPlay(post: Post) {} //Для запуска видео
    fun onPostFragment(post: Post) {} //HW 11 Fragment
}