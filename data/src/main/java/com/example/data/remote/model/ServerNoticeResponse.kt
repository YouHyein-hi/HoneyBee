package com.example.data.remote.model

import com.example.domain.model.remote.receive.notice.NoticeData

/**
 * 2023-08-20
 * pureum
 */
data class ServerNoticeResponse(
    val billNoticeId: String,
    val billNoticeUserName: String,
    val billNoticeTitle: String,
    val billNoticeDate: String,
    val billNoticeContent: String,
)

fun ServerNoticeResponse.toNoticeData(): NoticeData = NoticeData(
    id = billNoticeId,
    title = billNoticeTitle,
    date = billNoticeDate,
    content = billNoticeContent,
    userName = billNoticeUserName
)