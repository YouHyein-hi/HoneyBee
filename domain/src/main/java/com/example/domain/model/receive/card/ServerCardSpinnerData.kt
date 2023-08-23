package com.example.domain.model.receive.card

data class ServerCardSpinnerData(
    val status: String,
    val message: String,
    val body: List<CardSpinnerData>?
)