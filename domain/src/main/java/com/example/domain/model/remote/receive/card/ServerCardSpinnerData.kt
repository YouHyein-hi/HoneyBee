package com.example.domain.model.remote.receive.card

data class ServerCardSpinnerData(
    val status: String,
    val message: String,
    val body: List<CardSpinnerData>?
)

data class CardSpinnerData(
    var name:String,
    var amount:String
)