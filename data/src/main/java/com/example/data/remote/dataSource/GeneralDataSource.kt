package com.example.data.remote.dataSource

import com.example.data.remote.model.ServerResponse
import com.example.domain.model.receive.BillData
import com.example.domain.model.receive.PictureData
import com.example.domain.model.receive.SimpleResponse
import okhttp3.MultipartBody
import retrofit2.http.*
import java.time.LocalDateTime

/**
 * 2023-07-23
 * pureum
 */
interface GeneralDataSource {
    @Multipart
    @POST("bill/add")
    suspend fun sendDataSource(
        @Part cardName: MultipartBody.Part,
        @Part storeName: MultipartBody.Part,
        @Part billSubmitTime: MultipartBody.Part,
        @Part amount: MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ): ServerResponse<SimpleResponse>

    @Streaming
    @GET("bill/list")   // 전체 데이터 요청
    suspend fun receiveDataSource(): ServerResponse<BillData>

    @Streaming
    @GET("bill/image/{id}")
    suspend fun receivePictureDataSource(
        @Path("id") user: String
    ): ServerResponse<SimpleResponse>

    @DELETE("bill/delete/{uid}")
    suspend fun deleteServerData(
        @Path("uid") uid: Long
    ): ServerResponse<SimpleResponse>

    //    @PUT("bill/update/{id}/{cardName}/{storeName}/{date}/{amount}")
    @FormUrlEncoded
    @PUT("bill/update/{id}")
    suspend fun updateDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName: String,
        @Field("storeName") storeName: String,
        @Field("billSubmitTime") billSubmitTime: LocalDateTime,
        @Field("amount") amount: Int,
    ): ServerResponse<SimpleResponse>
}