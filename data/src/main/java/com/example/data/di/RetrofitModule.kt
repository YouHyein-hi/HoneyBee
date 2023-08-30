package com.example.data.di

import com.example.data.manager.PreferenceManager
import com.example.data.util.NetworkInterceptor
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


    @Singleton
    @Provides
    fun provideInterceptor(preferenceManager: PreferenceManager): NetworkInterceptor =
        NetworkInterceptor(preferenceManager)

    @Singleton
    @Provides
    fun provideOkhttp(networkInterceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideSendRetrofit(interceptorOkHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://210.119.104.158:8080/")
            .client(interceptorOkHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //네트워크 통신 과정을 보기 위한 클라이언트
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
        .addInterceptor {
        val request = it.request()
            .newBuilder()
            .build()
        val response = it.proceed(request)
        response
    }
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20,TimeUnit.SECONDS)
        .writeTimeout(20,TimeUnit.SECONDS)
        .build()
}