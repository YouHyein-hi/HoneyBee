package com.example.domain.model.local

import androidx.core.net.toUri
import com.example.domain.model.ui.recycler.LocalRecyclerData

/**
 * 2023-02-15
 * pureum
 */
data class RoomData(
    val uid: String,
    val cardName: String,
    val storeAmount: String,
    val billSubmitTime: String,
    val storeName: String,
    val file: String,
)

fun RoomData.toRecyclerShowData(): LocalRecyclerData = LocalRecyclerData(uid, cardName, storeAmount, billSubmitTime, storeName, file.toUri())



