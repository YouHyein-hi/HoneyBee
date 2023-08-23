package com.example.data.repoImpl

import com.example.data.remote.dataSource.NoticeDataSource
import com.example.domain.model.receive.ServerNoticeData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.send.DomainAddNoticeData
import com.example.domain.repo.NoticeRepository
import com.example.domain.util.changeDate
import toServerNoticeData
import toServerResponseData
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
        val newList = result.body?.map { it.copy(date = changeDate(it.date)) }
        return ServerNoticeData(result.status, result.message, newList)
    }

    override suspend fun addNoticeRepository(addNoticeData: DomainAddNoticeData): ServerResponseData {
        return noticeDataSource.addNoticeDataSource(
            title = addNoticeData.title,
            date = addNoticeData.date,
            content = addNoticeData.content
        ).toServerResponseData()
    }
}