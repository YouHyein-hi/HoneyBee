package com.example.data.remote.model

import com.example.domain.model.receive.CardData

/**
 * 2023-08-20
 * pureum
 */
data class ServerCardResponse(
    var uid:Long,
    var cardName:String,
    var cardAmount:String,
    var billCheckDate:String
)

fun ServerCardResponse.toCardData() = CardData(uid = uid, name = cardName, amount = cardAmount, billCheckDate = billCheckDate)