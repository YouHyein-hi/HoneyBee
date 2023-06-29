package com.example.domain.model.receive

import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-06-24
 * pureum
 */
data class DomainUpadateData(
    val id : Long,
    val cardName: String,
    val amount: String,
    var billSubmitTime: LocalDateTime,
    val storeName: String,
)
