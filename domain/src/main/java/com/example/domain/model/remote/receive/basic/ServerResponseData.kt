package com.example.domain.model.remote.receive.basic

/**
 * 2023-08-19
 * pureum
 */
data class ServerResponseData(
    val status: String,
    val message: String,
    val body: String?
)

