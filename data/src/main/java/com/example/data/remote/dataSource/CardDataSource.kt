package com.example.data.remote.dataSource

import com.example.data.remote.model.ReceiveCardData
import com.example.data.remote.model.ServerCardResponse
import com.example.data.remote.model.ServerResponse
import com.example.domain.model.receive.CardData
import com.example.domain.model.receive.DomainServerResponse
import retrofit2.http.*

/**
 * 2023-07-23
 * pureum
 */
interface CardDataSource {

    @FormUrlEncoded
    @POST("billCard/add")
    suspend fun sendCardDataSource(
        @Field("cardName") cardName : String?,
        @Field("cardAmount") amount : Int?,
        @Field("billCheckDate") billCheckDate : String?
    ): DomainServerResponse

    @Streaming
    @GET("billCard/list")
    suspend fun getCardDataSource() : ServerCardResponse<CardData>

    @DELETE("billCard/delete/{uid}")
    suspend fun deleteCardDataSource(
        @Path("uid") uid:Long
    ): String

    @FormUrlEncoded
    @PUT("billCard/update/{id}")
    suspend fun updateCardDataSource(
        @Path("id") id: Long,
        @Field("cardName") cardName : String,
        @Field("cardAmount") cardAmount : Int
    ): ServerResponse
}