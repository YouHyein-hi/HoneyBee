package com.example.domain.model.remote.receive.card

/**
 * 2023-08-19
 * pureum
 */
data class ServerCardData(
    val status: String,
    val message: String,
    val body: List<CardData>?
)

data class CardData(
    var uid:Long,
    var cardName:String,
    var cardAmount:String,
    var cardExpireDate: String?,
    var billCheckDate:String?
)

