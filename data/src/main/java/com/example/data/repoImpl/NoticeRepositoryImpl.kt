package com.example.data.repoImpl

import com.example.data.remote.dataSource.NoticeDataSource
import com.example.data.remote.model.GetNoticeListData
import com.example.data.remote.model.toDomainGetNoticeListData
import com.example.domain.model.send.DomainAddNoticeData
import com.example.domain.model.send.DomainGetNoticeListData
import com.example.domain.repo.NoticeRepository
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class NoticeRepositoryImpl @Inject constructor(
    private val noticeDataSource: NoticeDataSource
): NoticeRepository {
    override suspend fun getNoticeListRepository(): MutableList<DomainGetNoticeListData> {
        return noticeDataSource.getNoticeListDataSource().map { it.toDomainGetNoticeListData() }.toMutableList()
    }

    override suspend fun addNoticeRepository(addNoticeData: DomainAddNoticeData): String {
        return noticeDataSource.addNoticeDataSource(
            title = addNoticeData.title,
            date = addNoticeData.date,
            content = addNoticeData.content
        )
    }
}