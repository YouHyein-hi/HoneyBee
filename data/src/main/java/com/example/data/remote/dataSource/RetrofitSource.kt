package com.example.data.remote.dataSource

import com.example.data.remote.dto.ReceiveData
import com.example.data.remote.dto.SendData
import retrofit2.http.GET

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitSource {

    @GET("character")
    suspend fun sendDataSource(
        card:String, date:String, picture:ByteArray
    ): SendData

    @GET("")
    suspend fun receiveDataSource():ReceiveData


}