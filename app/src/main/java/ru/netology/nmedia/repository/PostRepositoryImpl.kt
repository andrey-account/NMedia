package ru.netology.nmedia.repository

import androidx.paging.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.post.PostDao
import ru.netology.nmedia.dao.post.PostRemoteKeyDao
import ru.netology.nmedia.dao.wall.WallDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.*
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiException
import ru.netology.nmedia.error.NetworkException
import ru.netology.nmedia.error.UnknownException
import java.io.File
import java.io.IOException
import javax.inject.Inject


class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val apiService: ApiService,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb,
    private val wallDao: WallDao
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = PostRemoteMediator(
            service = apiService,
            appDb = appDb,
            postDao = postDao,
            postRemoteKeyDao = postRemoteKeyDao
        ),
        pagingSourceFactory = postDao::getPagingSource,
    ).flow
        .map { it.map(PostEntity::toDto) }

    override fun userWall(id: Long): Flow<PagingData<FeedItem>> {
        TODO("Not yet implemented")
    }
    override suspend fun likeById(post: Long) {
        TODO("Not yet implemented")
    }
    override suspend fun unlikeById(post: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun save(post: Post) {
        try {
            val response = apiService.createPost(post)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun saveWithAttachment(post: Post, media: File) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Long): Post? =
        wallDao.getPostById(id)?.toDto()

    override suspend fun wallRemoveById(id: Long) = wallDao.removeById(id)
}
