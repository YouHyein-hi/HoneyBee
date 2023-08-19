@file:JvmName("ServerResponseKt")

package com.example.data.remote.model
import com.example.domain.model.receive.CardData
import com.example.domain.model.receive.CardResponseData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.receive.SimpleResponse

/**
 * 2023-08-19
 * pureum
 */
data class ServerResponse<T>(
    val status: String,
    val message: String,
    val body: List<T>? =null
)

fun <T: CardData> ServerResponse<T>.toServerCardData() = CardResponseData(status, message, body)

fun ServerResponse<SimpleResponse>.toServerResponseData() = ServerResponseData(status, message, body)
