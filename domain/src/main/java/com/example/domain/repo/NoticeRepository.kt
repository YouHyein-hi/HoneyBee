package com.example.domain.repo

import com.example.domain.model.receive.ServerNoticeData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainAddNoticeData

/**
 * 2023-07-23
 * pureum
 */
interface NoticeRepository {
    suspend fun getNoticeListRepository(): ServerNoticeData
    suspend fun addNoticeRepository(addNoticeData: DomainAddNoticeData): ServerResponseData
}