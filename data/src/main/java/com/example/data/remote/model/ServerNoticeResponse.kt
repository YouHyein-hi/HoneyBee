package com.example.data.remote.model

import com.example.domain.model.receive.notice.NoticeData

/**
 * 2023-08-20
 * pureum
 */
data class ServerNoticeResponse(
    val billNoticeId: String,
    val billNoticeTitle: String,
    val billNoticeDate: String,
    val billNoticeContent: String,
)

fun ServerNoticeResponse.toNoticeData(): NoticeData = NoticeData(id = billNoticeId, title =  billNoticeTitle, date = billNoticeDate.toString(), content = billNoticeContent)