package com.example.domain.model.send

import java.time.LocalDateTime

/**
 * 2023-08-13
 * pureum
 */
data class DomainGetNoticeListData(
    val id: Int,
    val title: String,
    val date: LocalDateTime,
    val content: String,
)