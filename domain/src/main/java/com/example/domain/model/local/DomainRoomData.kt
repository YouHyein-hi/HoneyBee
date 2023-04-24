package com.example.domain.model.local

import com.example.domain.model.receive.DomainReceiveAllData

/**
 * 2023-02-15
 * pureum
 */
data class DomainRoomData(
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val file: String
)

fun DomainRoomData.toDomainRecyclerData(): DomainReceiveAllData = DomainReceiveAllData("0", cardName, amount, date, storeName, file)

