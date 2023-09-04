package com.example.domain.model.remote.send.notice

import java.time.LocalDateTime

/**
 * 2023-08-30
 * pureum
 */
data class SendNoticeAddData(
    val title: String,
    val date: LocalDateTime,
    val content: String
)
