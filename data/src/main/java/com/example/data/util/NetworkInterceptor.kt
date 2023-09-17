package com.example.data.util

import android.util.Log
import com.example.data.manager.PreferenceManager
import com.example.data.remote.dataSource.LoginDataSource
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * 2023-08-29
 * pureum
 */
class NetworkInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager,
) : Interceptor {
    init {
        Log.e("TAG", "NetworkInterceptor 생성", )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val firstRequest = requestMaker(request)
        Log.e("TAG", "firstRequest: $firstRequest", )

        //보낸 요청의 반환값
        val response = chain.proceed(firstRequest)
        Log.e("TAG", "intercept response: $response", )
        Log.e("TAG", "intercept response: ${response.body}", )

        //토큰이 문제(만료 등)가 있다는 401 에러일 경우 여기서 자동으로 재요청을 하여 토큰을 갈아껴야 함
        if (response.code == 401) {

            val newAccessToken = requestNewAccessToken()

            if (newAccessToken != null) {
                val secondRequest = requestMaker(firstRequest)
                return chain.proceed(secondRequest)
            }else{
                throw Exception("토큰 재요청 오류 ")
            }
        }
        return response
    }

    private fun requestMaker(request: Request): Request {
        Log.e("TAG", "requestMaker: requestMaker", )
        return request.newBuilder()
            .header("Authorization", preferenceManager.getAccessToken()?:"")
            .build()
    }

    private fun requestNewAccessToken(): String? {
        val response = Retrofit.Builder()
            .baseUrl("http://210.119.104.158:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginDataSource::class.java).requestNewAccessToken(
                accessToken = preferenceManager.getAccessToken()!!,
                refreshToken = preferenceManager.getRefreshToken()!!
            )
        Log.e("TAG", "gap: $response")
        Log.e("TAG", "gap?.headers(): ${response?.headers()}")
        Log.e("TAG", "Authorization: ${response?.headers()!!["Authorization"]}")
        Log.e("TAG", "RefreshToken: ${response.headers()["RefreshToken"]}")

        return if(response.isSuccessful){
            when(response.code()){
                200 -> response.headers()["Authorization"]
                else -> null
            }
        }else null
    }
}
