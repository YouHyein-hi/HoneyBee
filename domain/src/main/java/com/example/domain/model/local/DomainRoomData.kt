package com.example.domain.model.local

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.example.domain.model.RecyclerShowData
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
    val file: String,
    val uid: String
)

fun DomainRoomData.toRecyclerShowData(): RecyclerShowData = RecyclerShowData(uid, cardName, amount, date, storeName, file.toUri())

