package com.example.data.remote.dataSource

import com.example.data.remote.dto.ReceiveCardData
import com.example.data.remote.dto.ReceiveData
import com.example.data.remote.dto.SendData
import com.google.gson.annotations.SerializedName
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
    @POST("bill/add")
    suspend fun sendDataSource(
        @Part cardName : MultipartBody.Part,
        @Part amount : MultipartBody.Part,
        @Part storeName : MultipartBody.Part,
        @Part date : MultipartBody.Part,
        @Part file : MultipartBody.Part,
    ): String

    @GET("bills")   // 전체 데이터 요청
    suspend fun receiveDataSource() : MutableList<ReceiveData>

    @GET("test")
    suspend fun deleteServerData():String

    @Multipart
    @POST("bills/add")
    suspend fun sendCardDataSource(
        @Part cardName : MultipartBody.Part,
        @Part amount : MultipartBody.Part,
    ): String

    @GET("bills")
    suspend fun receiveCardDataSource() : MutableList<ReceiveCardData>

    @GET("bills")
    suspend fun deleteCardDataSource() : String

    @Multipart
    @POST("")
    suspend fun resendDataSource(
        @Part id : MultipartBody.Part,
        @Part cardName : MultipartBody.Part,
        @Part amount : MultipartBody.Part,
        @Part storeName : MultipartBody.Part,
        @Part date : MultipartBody.Part,
        @Part file : MultipartBody.Part,
    ):String

    @FormUrlEncoded
    @POST("")
    suspend fun resendCardDataSource(
        @Field("cardName") cardName : String,
        @Field("cardAmount") cardAmount : Int
    ):String



    @Multipart
    @POST("bill/test")
    suspend fun myTest(
        @Part file : MultipartBody.Part
    ):String
}