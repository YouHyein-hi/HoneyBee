package com.example.domain.model.receive

/**
 * 2023-08-21
 * pureum
 */
data class ServerStoreData(
    val status: String,
    val message: String,
    val body: List<String>?
)
