package com.example.data.di

import com.example.data.local.LocalDatabase
import com.example.data.remote.dataSource.*
import com.example.data.remote.dataSourceImpl.NoticeDataSourceImpl
import com.example.data.repoImpl.*
import com.example.domain.repo.*
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
//    @Provides
//    @Singleton
//    fun provideRepoReceiveData(source : RetrofitSource): RetrofitRepo {
//        return RetrofitRepoImpl(source)
//    }

    @Provides
    @Singleton
    fun provideCardRepoData(source : CardDataSource): CardRepository {
        return CardRepositoryImpl(source)
    }

    @Provides
    @Singleton
    fun provideGeneralData(source : GeneralDataSource): GeneralRepository {
        return GeneralRepositoryImpl(source)
    }

    @Provides
    @Singleton
    fun provideLoginData(source : LoginDataSource): LoginRepository {
        return LoginRepositoryImpl(source)
    }

    @Provides
    @Singleton
    fun provideNoticeData(source : NoticeDataSource): NoticeRepository {
        return NoticeRepositoryImpl(source)
    }

    @Provides
    @Singleton
    fun provideRepoRoom(source : LocalDatabase): RoomRepository {
        return RoomRepositoryImpl(source.myDatabase())
    }

}