package com.example.domain.model.remote.send.card

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 2023-08-30
 * pureum
 */
data class SendCardData(
    var cardName: String,
    var cardAmount: Int,
    var cardExpireDate: LocalDate,
    var cardDesignId: Int
)
