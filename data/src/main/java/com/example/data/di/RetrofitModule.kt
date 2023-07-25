package com.example.data.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 2023-02-02
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    var gson = GsonBuilder().setLenient().create()


//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class SendCard
//
//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class Api

    @Singleton
    @Provides
    fun provideSendRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://210.119.104.158:8080/")
//            .baseUrl("http://192.168.1.13:8080/")

//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }





    private val okHttpClient = OkHttpClient.Builder().
    addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).addInterceptor {
        val request = it.request()
            .newBuilder()
            .build()
        val response = it.proceed(request)
        response
    }.connectTimeout(20, TimeUnit.SECONDS).
    readTimeout(20,TimeUnit.SECONDS).
    writeTimeout(20,TimeUnit.SECONDS).
    build()
}