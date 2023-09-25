package com.example.data.util

import android.os.Looper
import android.util.Log
import com.example.data.BuildConfig
import com.example.data.di.RetrofitModule
import com.example.data.manager.PreferenceManager
import com.example.data.remote.dataSource.LoginDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * 2023-08-29
 * pureum
 */
class NetworkInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager,
) : Interceptor {

    private var gson: Gson = GsonBuilder().setLenient().create()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val firstRequest = requestMaker(request)
        val response : Response

        //프로그래스 바 UX를 고려해 통신 최소시간 할당
        val waitingTime = measureTimeMillis {
            //보낸 요청의 반환값
            response = chain.proceed(firstRequest)
        }.let {
            if(it<300L)
                runBlocking {
                    delay(300L-it)
                }
        }


        if (response.code == 401) {
            response.close()

            var newAccessToken:String? = ""

            //TODO 이 방법보다 나은 방법이 있을것같음..
            runBlocking {
                newAccessToken = requestNewAccessToken()
            }


            Log.e("TAG", "oldAccessToken: ${preferenceManager.getAccessToken()}")
            Log.e("TAG", "newAccessToken: $newAccessToken", )

            if (newAccessToken != null) {
                preferenceManager.putAccessToken(newAccessToken?:"")
                val secondRequest = requestMaker(firstRequest)
                return chain.proceed(secondRequest)
            }else{
                return response
            }
        }
        return response
    }

    private fun requestMaker(request: Request): Request {
        return request.newBuilder()
            .header("Authorization", preferenceManager.getAccessToken()?:"")
            .build()
    }

    private suspend fun requestNewAccessToken(): String? {
            val response = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(LoginDataSource::class.java).requestNewAccessToken(
                    accessToken = preferenceManager.getAccessToken(),
                    refreshToken = preferenceManager.getRefreshToken()
                )
            response
        return response?.headers()?.get("Authorization")
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
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()
}
