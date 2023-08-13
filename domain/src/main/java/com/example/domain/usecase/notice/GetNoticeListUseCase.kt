package com.example.domain.usecase.notice

import android.util.Log
import com.example.domain.model.send.DomainAddNoticeData
import com.example.domain.model.send.DomainGetNoticeListData
import com.example.domain.repo.NoticeRepository

/**
 * 2023-07-23
 * pureum
 */
class GetNoticeListUseCase(
    private val getNoticeUseCase: NoticeRepository
) {
    suspend operator fun invoke(): MutableList<DomainGetNoticeListData> {
        Log.e("TAG", "invoke", )
        val gap = getNoticeUseCase.getNoticeListRepository()
        Log.e("TAG", "invoke: $gap", )
        return gap
    }
}