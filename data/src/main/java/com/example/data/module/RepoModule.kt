package com.example.data.module

import com.example.data.remote.dataSource.ReceiveSource
import com.example.data.remote.dataSource.SendSource
import com.example.data.repoImpl.ReceiveRepoImpl
import com.example.data.repoImpl.SendRepoImpl
import com.example.domain.repo.ReceiveRepo
import com.example.domain.repo.SendRepo
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
    fun provideRepoSendData(source : SendSource): SendRepo {
        return SendRepoImpl(source)
    }

    @Provides
    @Singleton
    fun provideRepoReceiveData(source : ReceiveSource): ReceiveRepo {
        return ReceiveRepoImpl(source)
    }
}