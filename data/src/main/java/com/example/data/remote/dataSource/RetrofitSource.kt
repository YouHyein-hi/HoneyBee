package com.example.data.remote.dataSource

import com.example.data.remote.dto.ReceiveData
import com.example.data.remote.dto.SendData
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitSource {

    @Multipart
    @POST("uplaod/")
    suspend fun sendDataSource(
        date:LocalDateTime, amount : Int, card:String, picture:MultipartBody.Part //사진
    ): SendData

    @GET("")
    suspend fun receiveDataSource():ReceiveData


}