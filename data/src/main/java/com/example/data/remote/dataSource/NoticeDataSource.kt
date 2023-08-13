package com.example.data.remote.dataSource

import com.example.data.remote.model.GetNoticeListData
import com.example.data.remote.model.ServerResponse
import retrofit2.http.*
import java.time.LocalDateTime

/**
 * 2023-07-23
 * pureum
 */
interface NoticeDataSource {
    @Streaming
    @GET("billNotice/list")
    suspend fun getNoticeListDataSource(): MutableList<GetNoticeListData>

    @FormUrlEncoded
    @POST("billNotice/add")
    suspend fun addNoticeDataSource(
        @Field("billNoticeTitle") title : String,
        @Field("billNoticeDate") date : LocalDateTime,
        @Field("billNoticeContent") content : String
    ): String
}