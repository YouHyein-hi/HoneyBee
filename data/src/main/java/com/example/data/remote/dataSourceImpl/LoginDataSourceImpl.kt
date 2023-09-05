package com.example.data.remote.dataSourceImpl

import ServerResponse
import android.util.Log
import com.example.data.di.RetrofitModule
import com.example.data.remote.dataSource.LoginDataSource
import com.example.data.util.HeaderManager
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class LoginDataSourceImpl @Inject constructor(
    @RetrofitModule.Login private val retrofit: Retrofit,
    private val headerManager: HeaderManager
): LoginDataSource {
    override suspend fun requestLogin(email: String, password: String): Response<ServerResponse<String>>? {
        retrofit.create(LoginDataSource::class.java).requestLogin(
            email = email,
            password = password
        )?.let {
            if (it.isSuccessful) {
                val headers = it.headers()
                val body = it.body()
                return if(headerManager(headers))
                    Response.success(body)
                else
                    Response.error(401, it.errorBody())
            }
            throw Exception("연결 실패 - Response false")
        }
        throw Exception("연결 실패 - Null Err")
    }

    override fun requestNewAccessToken(
        accessToken: String,
        refreshToken: String
    ): Response<ServerResponse<String>>? {
        return retrofit.create(LoginDataSource::class.java)
            .requestNewAccessToken(accessToken = accessToken, refreshToken = refreshToken)
    }
}