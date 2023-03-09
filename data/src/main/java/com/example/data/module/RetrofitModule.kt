package com.example.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * 2023-02-02
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideSendRetrofit():Retrofit{
        return Retrofit.Builder()
//            .baseUrl("http://10.9.136.242:8080/bills/")
            .baseUrl("http://10.9.136.242:8080/bills/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}