package com.example.data.remote.dataSource

import ServerResponse
import com.example.data.di.DataSourceModule
import com.example.data.remote.model.ServerCardResponse
import dagger.Component
import retrofit2.http.*
import java.time.LocalDate
import javax.inject.Singleton

/**
 * 2023-07-23
 * pureum
 */

interface CardDataSource {


    @FormUrlEncoded
    @POST("bill/card/add")
    suspend fun sendCardDataSource(
        @Field("cardName") cardName : String,
        @Field("cardAmount") amount : Int,
        @Field("cardExpireDate") expireDate : LocalDate,
        @Field("cardDesignId") designId : Int
    ): ServerResponse<String>

    @Streaming
    @GET("bill/card/list")
    suspend fun getCardDataSource() : ServerResponse<List<ServerCardResponse>> //바디에 카드

    @DELETE("bill/delete/card/{uid}")
    suspend fun deleteCardDataSource(
        @Path("uid") uid:Long
    ): ServerResponse<Int>
//
//    //아직 안쓰이는 기능
//    @FormUrlEncoded
//    @PUT("billCard/update/{id}")
//    suspend fun updateCardDataSource(
//        @Path("id") id: Long,
//        @Field("cardName") cardName : String,
//        @Field("cardAmount") cardAmount : Int
//    ): ServerResponse<SimpleResponse>
}