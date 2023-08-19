package com.example.data.remote.model

import com.example.domain.model.receive.DomainServerResponse

/**
 * 2023-06-22
 * pureum
 */
data class ServerResponse(
    val status: String,
    val message: String,
    val body: List<Any>? = emptyList()
)

fun ServerResponse.toDomainServerResponse(): DomainServerResponse {
    return DomainServerResponse(status,message, body)
}