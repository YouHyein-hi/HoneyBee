package com.example.data.remote.dataSource

import ServerResponse
import com.example.data.di.DataSourceModule
import com.example.data.remote.dataSourceImpl.CardDataSourceImpl
import com.example.data.remote.dataSourceImpl.GeneralDataSourceImpl
import com.example.data.remote.model.ServerBillResponse
import dagger.Component
import dagger.Provides
import okhttp3.MultipartBody
import retrofit2.http.*
import java.time.LocalDateTime
import javax.inject.Singleton

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
    ): ServerResponse<Int>

    @Streaming
    @GET("bill/list")   // 전체 데이터 요청
    suspend fun getBillListDataSource(): ServerResponse<List<ServerBillResponse>>

    @Streaming
    @GET("bill/store/list")   // 전체 스토어 데이터 요청
    suspend fun getStoreListDataSource(): ServerResponse<List<String>>

    @Streaming
    @GET("bill/image/{id}")
    suspend fun getPictureDataSource(
        @Path("id") user: String
    ): ServerResponse<String>

    @DELETE("bill/delete/{uid}")
    suspend fun deleteServerData(
        @Path("uid") uid: Long
    ): ServerResponse<Int>

    @FormUrlEncoded
    @PUT("bill/update/{id}")
    suspend fun updateDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName: String,
        @Field("storeName") storeName: String,
        @Field("billSubmitTime") billSubmitTime: LocalDateTime,
        @Field("amount") amount: Int,
    ): ServerResponse<Int>
}