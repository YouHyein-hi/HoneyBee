package com.example.data.remote.dataSource

import com.example.data.remote.model.ReceiveData
import com.example.data.remote.model.ServerResponse
import okhttp3.MultipartBody
import retrofit2.http.*
import java.time.LocalDateTime

/**
 * 2023-07-23
 * pureum
 */
interface GeneralDataSurce {
    @Multipart
    @POST("bill/add")
    suspend fun sendDataSource(
        @Part cardName : MultipartBody.Part,
        @Part storeName : MultipartBody.Part,
        @Part billSubmitTime : MultipartBody.Part,
        @Part amount : MultipartBody.Part,
        @Part file : MultipartBody.Part,
    ): String

    @Streaming
    @GET("bill/list")   // 전체 데이터 요청
    suspend fun receiveDataSource() : MutableList<ReceiveData>

    @Streaming
    @GET("bill/image/{id}")
    suspend fun receivePictureDataSource(
        @Path("id") user:String
    ) : String

    @DELETE("bill/delete/{uid}")
    suspend fun deleteServerData(
        @Path("uid") uid:Long
    ): String

    //    @PUT("bill/update/{id}/{cardName}/{storeName}/{date}/{amount}")
    @FormUrlEncoded
    @PUT("bill/update/{id}")
    suspend fun updateDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName: String,
        @Field("storeName") storeName: String,
        @Field("billSubmitTime") billSubmitTime: LocalDateTime,
        @Field("amount") amount: String,
    ): ServerResponse

}