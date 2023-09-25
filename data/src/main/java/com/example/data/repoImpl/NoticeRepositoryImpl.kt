package com.example.data.repoImpl

import android.util.Log
import com.example.data.mapper.ResponseMapper.toServerNoticeData
import com.example.data.mapper.ResponseMapper.toServerResponseData
import com.example.data.remote.dataSource.NoticeDataSource
import com.example.domain.model.remote.receive.notice.ServerNoticeData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.notice.SendNoticeAddData
import com.example.domain.repo.NoticeRepository
import com.example.domain.util.StringUtil
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class NoticeRepositoryImpl @Inject constructor(
    private val noticeDataSource: NoticeDataSource
): NoticeRepository {
    override suspend fun getNoticeListRepository(): ServerNoticeData {
        val result = noticeDataSource.getNoticeListDataSource().toServerNoticeData()
        val newList = result.body?.map { it.copy(date = StringUtil.changeDate(it.date)) }
        return ServerNoticeData(result.status, result.message, newList)
    }

    override suspend fun addNoticeRepository(sendNoticeAddData: SendNoticeAddData): ServerResponseData {
        return noticeDataSource.addNoticeDataSource(
            title = sendNoticeAddData.title,
//            date = sendNoticeAddData.date,
            content = sendNoticeAddData.content
        ).toServerResponseData()
    }
}