package com.example.domain.model

import android.net.Uri

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

fun DomainRoomData.toDomainRecyclerData():DomainReceiveAllData = DomainReceiveAllData(cardName, amount, date, storeName, file)

