package com.example.domain.model.remote.send.bill

import java.time.LocalDateTime

/**
 * 2023-08-30
 * pureum
 */
data class SendBillUpdateData(
    val id: Long,
    val cardName: String,
    val storeAmount: Int,
    var date: LocalDateTime,
    val storeName: String,
)
