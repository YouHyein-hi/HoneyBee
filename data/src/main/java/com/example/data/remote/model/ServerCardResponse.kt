package com.example.data.remote.model

import com.example.domain.model.remote.receive.card.CardData
import com.example.domain.model.remote.receive.card.CardSpinnerData

/**
 * 2023-08-20
 * pureum
 */
data class ServerCardResponse(
    var billCardId:Long,
    var cardName:String,
    var cardAmount:Int,
    var cardExpireDate: String,
    var cardDesignId: Int
)

fun ServerCardResponse.toCardData() =
    CardData(uid = billCardId, cardName = cardName, cardAmount = cardAmount.toString(), cardExpireDate= cardExpireDate, cardDesignId = cardDesignId)
fun ServerCardResponse.toCardSpinnerData() = CardSpinnerData(name = cardName, amount = cardAmount.toString())