package com.example.data.remote.dataSource

import com.example.data.remote.dto.ReceiveData
import com.example.data.remote.dto.SendData
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitSource {

    @POST("uplaod/")
    suspend fun sendDataSource(
        date:String, amount : Int, card:String, picture:ByteArray
    ): SendData

    @GET("")
    suspend fun receiveDataSource():ReceiveData


}