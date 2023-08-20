package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import java.io.File

interface PostRepository {

    val data: Flow<PagingData<FeedItem>>
    fun userWall(id: Long): Flow<PagingData<FeedItem>>
    suspend fun likeById(post: Long)
    suspend fun unlikeById(post: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, media: File)
    suspend fun removeById(id: Long)
    suspend fun getById(id: Long): Post?
    suspend fun wallRemoveById(id: Long)
}