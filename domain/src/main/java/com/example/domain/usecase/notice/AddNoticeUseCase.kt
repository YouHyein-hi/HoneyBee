package com.example.domain.usecase.notice

import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.notice.SendNoticeAddData
import com.example.domain.repo.NoticeRepository

/**
 * 2023-07-23
 * pureum
 */
class AddNoticeUseCase(
    private val addNoticeUseCase: NoticeRepository
) {
    suspend operator fun invoke(sendNoticeAddData: SendNoticeAddData): ServerResponseData {
        return addNoticeUseCase.addNoticeRepository(sendNoticeAddData)
    }
}