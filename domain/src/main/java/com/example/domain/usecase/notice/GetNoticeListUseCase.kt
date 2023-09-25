package com.example.domain.usecase.notice

import com.example.domain.model.remote.receive.notice.ServerNoticeData
import com.example.domain.repo.NoticeRepository

/**
 * 2023-07-23
 * pureum
 */
class GetNoticeListUseCase(
    private val getNoticeUseCase: NoticeRepository
) {
    suspend operator fun invoke(): ServerNoticeData =
        getNoticeUseCase.getNoticeListRepository()
}