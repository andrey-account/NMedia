package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getAll(): List<Post> //LiveData<List<Post>>
    fun save(post: Post)
    fun removeById(id: Long) //Для удаления постов

    fun likeById(post: Post)
    fun shareById(id: Long)
    fun look(id: Long)
}