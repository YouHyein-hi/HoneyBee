package com.example.data.di

import com.example.data.remote.dataSource.*
import com.example.data.remote.dataSourceImpl.*
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

//    @Singleton
//    @Provides
//    fun provideDataSourceReceiveData(retrofit: Retrofit):RetrofitSource = RetrofitSourceImpl(retrofit)

    @Singleton
    @Provides
    fun provideCardDataSource(retrofit: Retrofit):CardDataSource = CardDataSourceImpl(retrofit)

    @Singleton
    @Provides
    fun provideGeneralDataSource(retrofit: Retrofit):GeneralDataSurce = GeneralDataSourceImpl(retrofit)

    @Singleton
    @Provides
    fun provideLoginSource(retrofit: Retrofit):LoginDataSource = LoginDataSourceImpl(retrofit)

//    @Singleton
//    @Provides
//    fun provideNoticeSource(retrofit: Retrofit):NoticeDataSource = NoticeDataSourceImpl()


}