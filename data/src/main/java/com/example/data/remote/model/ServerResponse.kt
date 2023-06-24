package com.example.data.remote.model

import com.example.domain.model.receive.DomainServerReponse

/**
 * 2023-06-22
 * pureum
 */
data class ServerResponse(
    val status: String,
    val message: String,
)

fun ServerResponse.toDomainLoginResponse(): DomainServerReponse {
    return DomainServerReponse(status,message)
}