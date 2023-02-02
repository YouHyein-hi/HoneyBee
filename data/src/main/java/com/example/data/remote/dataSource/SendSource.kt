package com.example.data.remote.dataSource

import com.example.data.remote.dto.SendData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 2023-02-02
 * pureum
 */
interface SendSource {
    @GET("character")
    suspend fun sendDataSource(
    ):SendData


}