package com.example.data.remote.dataSource

import ServerResponse
import com.example.data.remote.model.ServerNoticeResponse
import retrofit2.http.*
import java.time.LocalDateTime

/**
 * 2023-07-23
 * pureum
 */
interface NoticeDataSource {
    @Streaming
    @GET("billNotice/list")
    suspend fun getNoticeListDataSource(): ServerResponse<List<ServerNoticeResponse>>

    @FormUrlEncoded
    @POST("billNotice/add")
    suspend fun addNoticeDataSource(
        @Field("billNoticeTitle") title : String,
        @Field("billNoticeDate") date : LocalDateTime,
        @Field("billNoticeContent") content : String
    ): ServerResponse<String>
}