package com.example.domain.model.remote.receive.card

data class ServerCardDetailData(
    val status: String,
    val message: String,
    val body: CardDetailData?
)

data class CardDetailData(
    val billCardId : String,
    val cardName : String,
    val cardAmount : String,
    val cardDesignId : String,
    val cardExpireDate: String,
    val writerName : String,
    val writeDateTime : String,
    val modifierName : String,
    val modifyDateTime : String
)