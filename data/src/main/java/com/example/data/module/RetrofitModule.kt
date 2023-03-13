package com.example.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
            .baseUrl("http://10.9.138.16:8080/bills/")
            .addConverterFactory(GsonConverterFactory.create())
            //.client(okHttpClient)
            .build()
    }

    private val okHttpClient = OkHttpClient.Builder().
    addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).addInterceptor {
        // Request
        val request = it.request()
            .newBuilder()
//            .addHeader("jwt_token", SmartCampusApp.prefs.token?:"")
            .build()
        // Response
        val response = it.proceed(request)
        response
    }.connectTimeout(20, TimeUnit.SECONDS).
    readTimeout(20,TimeUnit.SECONDS).
    writeTimeout(20,TimeUnit.SECONDS).
    build()
}