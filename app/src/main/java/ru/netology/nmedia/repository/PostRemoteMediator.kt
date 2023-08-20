package ru.netology.nmedia.repository

import androidx.paging.*
import androidx.room.withTransaction
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.post.PostDao
import ru.netology.nmedia.dao.post.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostRemoteKeyEntity
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.enumeration.RemoteKeyType
import ru.netology.nmedia.error.ApiException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val service: ApiService,
    private val postDao: PostDao,
    private val appDb: AppDb,
    private val postRemoteKeyDao: PostRemoteKeyDao,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    postRemoteKeyDao.max()?.let { id ->
                        service.getPostsAfter(id, state.config.pageSize)
                    } ?: service.getLatestPosts(state.config.initialLoadSize)
                }
                LoadType.PREPEND -> return MediatorResult.Success(false)

                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getPostsBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiException(response.code(), response.message())

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        if (postRemoteKeyDao.isEmpty()) {
                            postRemoteKeyDao.clear()
                            postRemoteKeyDao.insert(
                                listOf(
                                    PostRemoteKeyEntity(
                                        type = RemoteKeyType.AFTER,
                                        key = body.first().id,
                                    ),
                                    PostRemoteKeyEntity(
                                        type = RemoteKeyType.BEFORE,
                                        key = body.last().id
                                    ),
                                )
                            )
                            postDao.clear()
                        } else {
                            postRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    type = RemoteKeyType.AFTER,
                                    key = body.first().id
                                )
                            )
                        }
                    }
                    LoadType.PREPEND -> {}
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = RemoteKeyType.BEFORE,
                                key = body.last().id,
                            )
                        )
                    }
                }
                postDao.insert(body.toEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}