package com.example.data.remote.dataSource

import com.example.data.remote.model.ReceiveCardData
import com.example.data.remote.model.ReceiveData
import okhttp3.MultipartBody
import retrofit2.http.*

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
        @Part storeName : MultipartBody.Part,
        @Part date : MultipartBody.Part,
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
    @GET("bill/picture/{uid}")
    suspend fun receivePictureDataSource(
        @Path("uid") user:String
    ) : ByteArray

    @DELETE("bill/delete/{uid}")
    suspend fun deleteServerData(
        @Path("uid") uid:Long
    ): String

    @DELETE("billCard/delete/{uid}")
    suspend fun deleteCardDataSource(
        @Path("uid") uid:Long
    ): String


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