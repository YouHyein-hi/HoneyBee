package com.example.domain.model.remote.receive.notice

/**
 * 2023-08-20
 * pureum
 */
data class ServerNoticeData(
val status: String,
val message: String,
val body: List<NoticeData>?
)

data class NoticeData(
    val id: String,
    val title: String,
    val date: String,
    val content: String,
)


