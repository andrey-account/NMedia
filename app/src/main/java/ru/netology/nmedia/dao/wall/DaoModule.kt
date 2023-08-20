package ru.netology.nmedia.dao.wall

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.dao.post.PostDao
import ru.netology.nmedia.dao.post.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb

class DaoModule {
    @InstallIn(SingletonComponent::class)
    @Module
    object DaoModule {
        @Provides
        fun providePostDao(db: AppDb): PostDao = db.postDao()

        @Provides
        fun providePostRemoteKeyDao(db: AppDb): PostRemoteKeyDao = db.postRemoteKeyDao()
    }
}