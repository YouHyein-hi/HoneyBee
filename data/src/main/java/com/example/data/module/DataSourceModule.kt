package com.example.data.module

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dataSourceImpl.RetrofitSourceImpl
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
    fun provideDataSourceReceiveData(retrofit: Retrofit):RetrofitSource = RetrofitSourceImpl(retrofit)
}