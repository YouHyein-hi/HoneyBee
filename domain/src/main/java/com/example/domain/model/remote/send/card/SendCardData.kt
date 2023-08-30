package com.example.domain.model.remote.send.card

/**
 * 2023-08-30
 * pureum
 */
data class SendCardData(
    var cardName: String,
    var cardAmount: Int,
    var billCheckDate: String
)
