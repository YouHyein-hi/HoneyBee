package com.example.data.remote.dataSource

import ServerResponse
import com.example.data.di.DataSourceModule
import com.example.data.remote.dataSourceImpl.CardDataSourceImpl
import com.example.data.remote.dataSourceImpl.NoticeDataSourceImpl
import com.example.data.remote.model.ServerNoticeResponse
import dagger.Component
import retrofit2.http.*
import java.time.LocalDateTime
import javax.inject.Singleton

/**
 * 2023-07-23
 * pureum
 */
interface NoticeDataSource {

    @Streaming
    @GET("bill/notice/list")
    suspend fun getNoticeListDataSource(): ServerResponse<List<ServerNoticeResponse>>

    @FormUrlEncoded
    @POST("bill/notice/add")
    suspend fun addNoticeDataSource(
        @Field("billNoticeTitle") title : String,
        @Field("billNoticeDate") date : LocalDateTime,
        @Field("billNoticeContent") content : String,
        @Field("billNoticeUserName") name : String = "푸름"
    ): ServerResponse<String>
}