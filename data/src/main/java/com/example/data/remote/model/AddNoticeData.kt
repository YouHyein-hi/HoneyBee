package com.example.data.remote.model

import com.example.domain.model.send.DomainAddNoticeData
import java.time.LocalDateTime

/**
 * 2023-08-13
 * pureum
 */
data class AddNoticeData(
    val billNoticeTitle: String,
    val billNoticeDate: LocalDateTime,
    val billNoticeContent: String
)

fun AddNoticeData.toDomainAddNoticeData():DomainAddNoticeData = DomainAddNoticeData(
    title = billNoticeTitle,
    date = billNoticeDate,
    content = billNoticeContent
)