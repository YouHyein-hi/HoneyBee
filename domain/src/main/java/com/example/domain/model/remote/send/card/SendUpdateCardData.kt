package com.example.domain.model.remote.send.card

/**
 * 2023-08-30
 * pureum
 */
data class SendUpdateCardData(
    val cardName: String,
    val amount: String,
    var billSubmitTime: String,
    val storeName: String,
)
