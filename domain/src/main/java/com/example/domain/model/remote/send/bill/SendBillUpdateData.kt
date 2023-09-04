package com.example.domain.model.remote.send.bill

import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-08-30
 * pureum
 */
data class SendBillUpdateData(
    val id: Long,
    val cardName: String,
    val storeName: String,
    var billSubmitTime: LocalDateTime,
    val amount: Int,
)
