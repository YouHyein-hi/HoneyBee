package com.example.domain.model.receive

/**
 * 2023-08-19
 * pureum
 */
data class CardResponseData(
    val status: String,
    val message: String,
    val body: List<CardData>
)

data class CardData(
    var uid:Long,
    var cardName:String,
    var cardAmount:String,
    var billCheckDate:String
)