package com.example.data.remote.model

import com.example.domain.model.receive.DomainReceiveAllData
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val file: String
)

fun ReceiveData.toDomainReceiveData() = DomainReceiveAllData(uid, cardName, amount, date, storeName, file)