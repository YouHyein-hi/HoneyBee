package com.example.data.remote.dataSource

import ServerResponse
import com.example.data.remote.model.ServerCardDetailResponse
import com.example.data.remote.model.ServerCardResponse
import com.example.data.remote.model.ServerDetailBillResponse
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface CardDataSource {


    @FormUrlEncoded
    @POST("bill/card/add")
    suspend fun sendCardDataSource(
        @Field("cardName") cardName : String,
        @Field("cardAmount") amount : Int,
        @Field("cardExpireDate") expireDate : LocalDate,
        @Field("cardDesignId") designId : Int
    ): Response<ServerResponse<String>>

    @Streaming
    @GET("bill/card/list")
    suspend fun getCardDataSource() : ServerResponse<List<ServerCardResponse>> //바디에 카드

    @Streaming
    @GET("bill/card/detail/{id}")
    suspend fun getDetailCardDataSource(
        @Path("id") id: String,
    ): ServerResponse<ServerCardDetailResponse>

    @DELETE("bill/delete/card/{uid}")
    suspend fun deleteCardDataSource(
        @Path("uid") uid:Long
    ): ServerResponse<Int>

//    //아직 안쓰이는 기능
    @FormUrlEncoded
    @POST("bill/card/update/{id}")
    suspend fun updateCardDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName : String,
        @Field("cardAmount") cardAmount : Int,
        @Field("cardExpireDate") cardExpireDate : LocalDate,
        @Field("cardDesignId") cardDesignId : Int,
    ): ServerResponse<Int>

}