package com.example.domain.model.receive

/**
 * 2023-06-23
 * pureum
 */
data class DomainServerResponse(
    val status: String,
    val message: String,
    val body: List<Any>? = emptyList()
)

