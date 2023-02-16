package com.example.data.module

import com.example.data.local.LocalDatabase
import com.example.data.local.dao.MyDao
import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.repoImpl.RetrofitRepoImpl
import com.example.data.repoImpl.RoomRepoImpl
import com.example.domain.repo.RetrofitRepo
import com.example.domain.repo.RoomRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 2023-02-02
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideRepoReceiveData(source : RetrofitSource): RetrofitRepo {
        return RetrofitRepoImpl(source)
    }

    @Provides
    @Singleton
    fun provideRepoRoom(source : LocalDatabase): RoomRepo {
        return RoomRepoImpl(source.myDatabase())
    }

}