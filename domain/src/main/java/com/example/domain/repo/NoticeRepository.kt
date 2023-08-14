package com.example.domain.repo

import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainAddNoticeData
import com.example.domain.model.send.DomainGetNoticeListData

/**
 * 2023-07-23
 * pureum
 */
interface NoticeRepository {
    suspend fun getNoticeListRepository(): MutableList<DomainGetNoticeListData>
    suspend fun addNoticeRepository(addNoticeData: DomainAddNoticeData): String
}