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
    @POST("/uplaod")
    suspend fun sendDataSource(
//        @PartMap data:HashMap<String, RequestBody>,
        @Part("cardName") card : String,
        @Part("amount") amount : Int,
        @Part("pictureName") pictureName:String,
        @Part("timestmap") date:LocalDateTime,
        @Part bill:MultipartBody.Part,
    ): SendData

    @GET("")
    suspend fun receiveDataSource():ReceiveData

    @GET("test")
    suspend fun test(
        @Query("a") a:String
    ):String


}