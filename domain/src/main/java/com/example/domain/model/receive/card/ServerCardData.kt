package com.example.domain.model.receive.card

/**
 * 2023-08-19
 * pureum
 */
data class ServerCardData(
    val status: String,
    val message: String,
    val body: List<CardData>?
)

