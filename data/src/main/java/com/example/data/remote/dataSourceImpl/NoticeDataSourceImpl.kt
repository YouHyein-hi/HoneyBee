package com.example.data.remote.dataSourceImpl

import android.util.Log
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
): NoticeDataSource {
    override suspend fun getNoticeListDataSource(): MutableList<GetNoticeListData> {
        Log.e("TAG", "getNoticeListDataSource", )
        val gap = retrofit.create(NoticeDataSource::class.java).getNoticeListDataSource()
        Log.e("TAG", "getNoticeListDataSource: $gap", )
        return gap
    }

    override suspend fun addNoticeDataSource(
        title: String,
        date: LocalDateTime,
        content: String
    ): String {
        return retrofit.create(NoticeDataSource::class.java).addNoticeDataSource(
            title= title,
            date = date,
            content = content
        )
    }
}