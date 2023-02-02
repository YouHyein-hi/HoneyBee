package com.example.data.remote.dto

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val pureum : String
)

fun ReceiveData.toDomainReceiveData() = DomainReceiveData(pureum)