package com.example.domain.usecase.notice

import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.send.DomainAddNoticeData
import com.example.domain.repo.NoticeRepository

/**
 * 2023-07-23
 * pureum
 */
class AddNoticeUseCase(
    private val addNoticeUseCase: NoticeRepository
) {
    suspend operator fun invoke(addNoticeData: DomainAddNoticeData): ServerResponseData {
        return addNoticeUseCase.addNoticeRepository(addNoticeData)
    }
}