package com.example.data.util

import android.util.Log
import com.example.data.manager.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * 2023-08-29
 * pureum
 */
class NetworkInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val firstRequest = requestMaker(preferenceManager.getAccessToken(), request)

        //보낸 요청의 반환값
        val response = chain.proceed(firstRequest)
        Log.e("TAG", "intercept 반환값: $response")

        //토큰이 문제(만료 등)가 있다는 401 에러일 경우 여기서 자동으로 재요청을 하여 토큰을 갈아껴야 함
        if (response.code == 401) {

            //새 토큰을 얻어오는 부분
            val newAccessToken = requestNewAccessToken(request)

            //다시 기존 요청을 수행하는 부분
            if (newAccessToken != null) {
                val secondRequest = requestMaker(preferenceManager.getRefreshToken(), firstRequest)
                return chain.proceed(secondRequest)
            }
        }
        return response
    }


    //TODO 토큰이 만료됐을 경우 리프레쉬 토큰을 실어서 서버에 요청한 후, 새 엑세스 토큰 값을 반환받아야 함
    //TODO 이 부분은 서버 구현이 되어야 구현 가능
    private fun requestNewAccessToken(request: Request): String? {
        // 레트로핏으로 토큰 얻어오는것과, sharedpreference에 넣어주는 작업등을 해야함
        return request.newBuilder()
            .header("Authorization", preferenceManager.getRefreshToken() ?: "")
            .build().toString()
    }

    private fun requestMaker(token: String?, request: Request): Request =
        request
            .newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
}
