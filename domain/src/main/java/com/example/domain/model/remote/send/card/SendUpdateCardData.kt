package com.example.domain.model.remote.send.card

import java.time.LocalDate

data class SendUpdateCardData (
    val id: Long,
    val cardName: String,
    val cardAmount: Int,
    var cardExpireDate: LocalDate,
    val cardDesignId: Int,
)