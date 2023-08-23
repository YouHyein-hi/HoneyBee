package com.example.domain.model.receive.notice

/**
 * 2023-08-20
 * pureum
 */
data class ServerNoticeData(
val status: String,
val message: String,
val body: List<NoticeData>?
)


