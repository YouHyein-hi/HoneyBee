package com.example.data.remote.dataSource

import ServerResponse
import android.net.Uri
import com.example.data.di.DataSourceModule
import com.example.data.remote.dataSourceImpl.CardDataSourceImpl
import com.example.data.remote.dataSourceImpl.GeneralDataSourceImpl
import com.example.data.remote.model.ServerBillResponse
import com.example.data.remote.model.ServerDetailBillResponse
import dagger.Component
import dagger.Provides
import okhttp3.MultipartBody
import retrofit2.http.*
import java.time.LocalDateTime
import javax.inject.Singleton

interface GeneralDataSource {

    @Multipart
    @POST("bill/add")
    suspend fun sendDataSource(
        @Part cardName: MultipartBody.Part,
        @Part storeName: MultipartBody.Part,
        @Part billSubmitTime: MultipartBody.Part,
        @Part amount: MultipartBody.Part,
        @Part file: MultipartBody.Part,
        @Part billMemo: MultipartBody.Part
    ): ServerResponse<Int>

    @Streaming
    @GET("bill/list")   // 전체 데이터 요청
    suspend fun getBillListDataSource(): ServerResponse<List<ServerBillResponse>>

    @Streaming
    @GET("bill/detail/{id}")
    suspend fun getDetailBillData(
        @Path("id") id: String,
    ): ServerResponse<ServerDetailBillResponse>

    @Streaming
    @GET("bill/store/list")   // 전체 스토어 데이터 요청
    suspend fun getStoreListDataSource(): ServerResponse<List<String>>

    @Streaming
    @GET("bill/image/{id}")
    suspend fun getPictureDataSource(
        @Path("id") id: String
    ): ServerResponse<String>

    @DELETE("bill/delete/{uid}")
    suspend fun deleteServerData(
        @Path("uid") uid: Long
    ): ServerResponse<Int>

    @FormUrlEncoded
    @POST("bill/update/{id}")
    suspend fun updateDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName: String,
        @Field("storeName") storeName: String,
        @Field("billSubmitTime") billSubmitTime: LocalDateTime,
        @Field("amount") amount: Int,
        @Field("billCheck") billCheck : Boolean,
        @Field("billMemo") billMemo : String
    ): ServerResponse<Int>

    @Streaming
    @GET("bill/check/{id}")   // 전체 스토어 데이터 요청
    suspend fun billCheckDataSource(
        @Path("id") id: Long
    ): ServerResponse<String>

}