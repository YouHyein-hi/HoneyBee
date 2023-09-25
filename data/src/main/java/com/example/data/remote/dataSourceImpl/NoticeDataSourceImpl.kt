package com.example.data.remote.dataSourceImpl

import ServerResponse
import com.example.data.di.RetrofitModule
import com.example.data.remote.dataSource.NoticeDataSource
import com.example.data.remote.model.ServerNoticeResponse
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class NoticeDataSourceImpl @Inject constructor(
    @RetrofitModule.Api private val retrofit: Retrofit
): NoticeDataSource {
    override suspend fun getNoticeListDataSource(): ServerResponse<List<ServerNoticeResponse>> =
        retrofit.create(NoticeDataSource::class.java).getNoticeListDataSource()


    override suspend fun addNoticeDataSource(
        title: String,
        content: String,
    ): ServerResponse<String> =
        retrofit.create(NoticeDataSource::class.java).addNoticeDataSource(
            title = title,
            content = content
        )

}