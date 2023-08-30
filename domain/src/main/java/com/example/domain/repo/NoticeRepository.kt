package com.example.domain.repo

import com.example.domain.model.remote.receive.notice.ServerNoticeData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.notice.SendNoticeAddData

/**
 * 2023-07-23
 * pureum
 */
interface NoticeRepository {
    suspend fun getNoticeListRepository(): ServerNoticeData
    suspend fun addNoticeRepository(sendNoticeAddData: SendNoticeAddData): ServerResponseData
}