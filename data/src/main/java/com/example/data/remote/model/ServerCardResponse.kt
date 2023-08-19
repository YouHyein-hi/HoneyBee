package com.example.data.remote.model
import com.example.domain.model.receive.CardData
import com.example.domain.model.receive.CardResponseData

/**
 * 2023-08-19
 * pureum
 */
data class ServerCardResponse<T>(
    val status: String,
    val message: String,
    val body: List<T>? = null
)

fun <T: CardData> ServerCardResponse<T>.toServerCardData() = CardResponseData(status, message, body as List<T>)
