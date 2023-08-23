package com.example.domain.model.receive.card

import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-06-24
 * pureum
 */
data class DomainUpadateData(
    val id : Long,
    val cardName: String,
    val amount: Int,
    var billSubmitTime: LocalDateTime,
    val storeName: String,
)
