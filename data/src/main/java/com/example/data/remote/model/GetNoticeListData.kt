package com.example.data.remote.model

import com.example.domain.model.send.DomainGetNoticeListData

/**
 * 2023-08-13
 * pureum
 */
data class GetNoticeListData(
    val billNoticeId: String,
    val billNoticeTitle: String,
    val billNoticeDate: String,
    val billNoticeContent: String,
)

fun GetNoticeListData.toDomainGetNoticeListData(): DomainGetNoticeListData =
    DomainGetNoticeListData(
        billNoticeId,
        billNoticeTitle,
        billNoticeDate,
        billNoticeContent
)
