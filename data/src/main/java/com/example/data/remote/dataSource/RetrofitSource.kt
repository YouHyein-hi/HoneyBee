package com.example.data.remote.dataSource

import com.example.data.remote.dto.ReceiveData
import com.example.data.remote.dto.SendData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitSource {

    //multipart에 쓰이는 모든 요소들은 @Part를 붙여줘야 함
    @Multipart
    @POST("bills/add")
    suspend fun sendDataSource(
        @Part cardName : MultipartBody.Part,
        @Part amount : MultipartBody.Part,
        @Part pictureName : MultipartBody.Part,
        @Part timestmap : MultipartBody.Part,
        @Part bill : MultipartBody.Part,
    ): String

    @GET("bills")
    suspend fun receiveDataSource():List<ReceiveData>

    @GET("test")
    suspend fun deleteServerData():String


}