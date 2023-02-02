package com.example.data.remote.dataSource

import com.example.data.remote.dto.ReceiveData
import retrofit2.http.GET

/**
 * 2023-02-02
 * pureum
 */
interface ReceiveSource {
    @GET("")
    suspend fun receiveDataSource():ReceiveData
}