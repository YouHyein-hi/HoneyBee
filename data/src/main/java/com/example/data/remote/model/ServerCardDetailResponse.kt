package com.example.data.remote.model

import com.example.domain.model.remote.receive.card.CardDetailData

data class ServerCardDetailResponse(
    val billCardId: String,
    val cardName: String,
    val cardAmount: String,
    val cardDesignId: String,
    val cardExpireDate: String,
    val writerName: String,
    val writeDateTime: String,
    val modifierName: String,
    val modifyDateTime: String
)

fun ServerCardDetailResponse.toServerCardDetailData(): CardDetailData =
    CardDetailData(
        billCardId = billCardId,
        cardName = cardName,
        cardAmount = cardAmount,
        cardDesignId = cardDesignId,
        cardExpireDate = cardExpireDate,
        writerName = writerName,
        writeDateTime = writeDateTime,
        modifierName = modifierName,
        modifyDateTime = modifyDateTime
    )
