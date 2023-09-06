package com.example.data.remote.dataSource

import ServerResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 2023-07-23
 * pureum
 */

interface LoginDataSource {
    @FormUrlEncoded
    @POST("auth/app/login")
    suspend fun requestLogin(
        @Field("userEmail") email : String,
        @Field("password") password : String,
    ): Response<ServerResponse<String>>?

    @POST("auth/app/reissue")
    fun requestNewAccessToken(
        @Header("Authorization") accessToken: String,
        @Header("RefreshToken") refreshToken: String
    ): Response<ServerResponse<String>>?
}