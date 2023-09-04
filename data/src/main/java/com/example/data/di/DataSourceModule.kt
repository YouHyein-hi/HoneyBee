package com.example.data.di

import com.example.data.remote.dataSource.*
import com.example.data.remote.dataSourceImpl.*
import com.example.data.util.HeaderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 2023-02-02
 * pureum
 */

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun provideCardDataSource(@RetrofitModule.Api retrofit: Retrofit):CardDataSource = CardDataSourceImpl(retrofit)

    @Singleton
    @Provides
    fun provideGeneralDataSource(@RetrofitModule.Api retrofit: Retrofit):GeneralDataSource = GeneralDataSourceImpl(retrofit)

    @Singleton
    @Provides
    fun provideLoginDataSource(@RetrofitModule.Login retrofit: Retrofit, headerManager: HeaderManager):LoginDataSource = LoginDataSourceImpl(retrofit,headerManager)

    @Singleton
    @Provides
    fun provideNoticeDataSource(@RetrofitModule.Api retrofit: Retrofit):NoticeDataSource = NoticeDataSourceImpl(retrofit)
}