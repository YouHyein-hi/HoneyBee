package com.example.domain.model.send

import java.time.LocalDateTime

/**
 * 2023-08-13
 * pureum
 */
data class DomainGetNoticeListData(
    val id: String,
    val title: String,
    val date: String,
    val content: String,
)