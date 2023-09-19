package com.example.data.remote.dataSourceImpl

import ServerResponse
import android.util.Log
import com.example.data.di.RetrofitModule
import com.example.data.remote.dataSource.NoticeDataSource
import com.example.data.remote.model.ServerNoticeResponse
import retrofit2.Retrofit
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class NoticeDataSourceImpl @Inject constructor(
    @RetrofitModule.Api private val retrofit: Retrofit
): NoticeDataSource {
    override suspend fun getNoticeListDataSource(): ServerResponse<List<ServerNoticeResponse>> {
        val gap = retrofit.create(NoticeDataSource::class.java).getNoticeListDataSource()
        Log.e("TAG", "getNoticeListDataSource 공지사항 리스트: $gap", )
        return gap
    }

    override suspend fun addNoticeDataSource(
        title: String,
        date: LocalDateTime,
        content: String,
        name:String
    ): ServerResponse<String> {
        Log.e("TAG", "addNoticeDataSource: $name", )
        val gap = retrofit.create(NoticeDataSource::class.java).addNoticeDataSource(
            title = title,
            date = date,
            content = content
        )
        Log.e("TAG", "addNoticeDataSource: $gap", )
        return gap
//        return retrofit.create(NoticeDataSource::class.java).addNoticeDataSource(
//            title = title,
//            date = date,
//            content = content
//        )
    }
}