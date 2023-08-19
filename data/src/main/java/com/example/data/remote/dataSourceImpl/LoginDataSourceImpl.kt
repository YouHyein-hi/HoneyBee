package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.LoginDataSource
import com.example.data.remote.model.ServerResponse
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class LoginDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
): LoginDataSource {
    override suspend fun requestLogin(email: String, password: String): ServerResponse<com.example.domain.model.receive.SimpleResponse> {
        return retrofit.create(LoginDataSource::class.java).requestLogin(
            email = email,
            password = password
        )
    }
}