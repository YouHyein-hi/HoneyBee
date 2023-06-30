package com.example.domain.model.local

import androidx.core.net.toUri
import com.example.domain.model.RecyclerShowData

/**
 * 2023-02-15
 * pureum
 */
data class DomainRoomData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val billSubmitTime: String,
    val storeName: String,
    val file: String,
)

fun DomainRoomData.toRecyclerShowData(): RecyclerShowData = RecyclerShowData(uid, cardName, amount, billSubmitTime, storeName, file.toUri())



