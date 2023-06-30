package com.example.data.remote.dataSource

import com.example.data.remote.model.ServerResponse
import com.example.data.remote.model.ReceiveCardData
import com.example.data.remote.model.ReceiveData
import okhttp3.MultipartBody
import retrofit2.http.*
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitSource {
    @Multipart
    @POST("bill/add")
    suspend fun sendDataSource(
        @Part cardName : MultipartBody.Part,
        @Part storeName : MultipartBody.Part,
        @Part billSubmitTime : MultipartBody.Part,
        @Part amount : MultipartBody.Part,
        @Part file : MultipartBody.Part,
    ): String

    @FormUrlEncoded
    @POST("billCard/add")
    suspend fun sendCardDataSource(
        @Field("cardName") cardName : String?,
        @Field("cardAmount") amount : Int?,
    ): String

    @Streaming
    @GET("bill/list")   // 전체 데이터 요청
    suspend fun receiveDataSource() : MutableList<ReceiveData>

    @Streaming
    @GET("billCard/list")
    suspend fun receiveCardDataSource() : MutableList<ReceiveCardData>

    @Streaming
    @GET("bill/image/{id}")
    suspend fun receivePictureDataSource(
        @Path("id") user:String
    ) : String

    @DELETE("bill/delete/{uid}")
    suspend fun deleteServerData(
        @Path("uid") uid:Long
    ): String

    @DELETE("billCard/delete/{uid}")
    suspend fun deleteCardDataSource(
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
    ):ServerResponse

    @FormUrlEncoded
    @PUT("billCard/update/{id}")
    suspend fun updateCardDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName : String,
        @Field("cardAmount") cardAmount : Int
    ):ServerResponse

    @FormUrlEncoded
    @POST("bill/login")
    suspend fun requestLogin(
        @Field("email") email : String,
        @Field("password") password : String,
    ): ServerResponse

    @Multipart
    @POST("bill/test")
    suspend fun myTest(
        @Part file : MultipartBody.Part
    ):String
}