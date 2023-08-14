package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.NoticeDataSource
import com.example.data.remote.model.GetNoticeListData
import retrofit2.Retrofit
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class NoticeDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : NoticeDataSource {
    override suspend fun getNoticeListDataSource(): MutableList<GetNoticeListData> {
        return retrofit.create(NoticeDataSource::class.java).getNoticeListDataSource()
    }

    override suspend fun addNoticeDataSource(
        title: String,
        date: LocalDateTime,
        content: String
    ): String {
        return retrofit.create(NoticeDataSource::class.java).addNoticeDataSource(
            title = title,
            date = date,
            content = content
        )
    }
}