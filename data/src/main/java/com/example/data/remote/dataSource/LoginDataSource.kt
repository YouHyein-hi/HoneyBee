package com.example.data.remote.dataSource

import com.example.data.remote.model.ServerResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 2023-07-23
 * pureum
 */
interface LoginDataSource {
    @FormUrlEncoded
    @POST("bill/login")
    suspend fun requestLogin(
        @Field("email") email : String,
        @Field("password") password : String,
    ): ServerResponse
}