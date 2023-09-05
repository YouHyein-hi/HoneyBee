package com.example.data.di

import com.example.data.manager.PreferenceManager
import com.example.data.util.NetworkInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * 2023-02-02
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
//            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            HttpLoggingInterceptor.Level.NONE
    }


    private var gson: Gson = GsonBuilder().setLenient().create()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Login

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Api

    @Singleton
    @Provides
    @Login
    fun provideLoginRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://210.119.104.158:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }




    @Singleton
    @Provides
    fun provideInterceptor(preferenceManager: PreferenceManager): NetworkInterceptor =
        NetworkInterceptor(preferenceManager)

    @Singleton
    @Provides
    fun provideOkhttpApi(networkInterceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Api
    fun provideApiRetrofit(interceptorClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://210.119.104.158:8080/")
            .client(interceptorClient)
            .client(okHttpClient)
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