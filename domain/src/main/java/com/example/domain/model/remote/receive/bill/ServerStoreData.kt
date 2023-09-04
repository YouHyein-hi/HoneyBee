package com.example.domain.model.remote.receive.bill

/**
 * 2023-08-21
 * pureum
 */
data class ServerStoreData(
    val status: String,
    val message: String,
    val body: List<String>?
)
