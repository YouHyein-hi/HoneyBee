package com.example.data.remote.model

import com.example.domain.model.send.DomainGetNoticeListData
import java.time.LocalDateTime

/**
 * 2023-08-13
 * pureum
 */
data class GetNoticeListData(
    val billNoticeId: Int,
    val billNoticeTitle: String,
    val billNoticeDate: LocalDateTime,
    val billNoticeContent: String,
)

fun GetNoticeListData.toDomainGetNoticeListData(): DomainGetNoticeListData =
    DomainGetNoticeListData(
        billNoticeId,
        billNoticeTitle,
        billNoticeDate,
        billNoticeContent
)
