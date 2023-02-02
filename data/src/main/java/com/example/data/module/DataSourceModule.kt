package com.example.data.module

import com.example.data.remote.dataSource.ReceiveSource
import com.example.data.remote.dataSource.SendSource
import com.example.data.remote.dataSourceImpl.ReceiveSourceImpl
import com.example.data.remote.dataSourceImpl.SendSourceImpl
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

    @Provides
    @Singleton
    fun provideDataSourceSendData(retrofit: Retrofit):SendSource{
        return SendSourceImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideDataSourceReceiveData(retrofit: Retrofit):ReceiveSource{
        return ReceiveSourceImpl(retrofit)
    }
}