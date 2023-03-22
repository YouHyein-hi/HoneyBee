package com.example.domain.model

import android.net.Uri
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-15
 * pureum
 */
data class DomainRoomData(
    val cardName: String,
    val amount: String,
    val date: String,
    val pictureName: String,
    val picture: String
)

fun DomainRoomData.toDomainRecyclerData():DomainRecyclerData = DomainRecyclerData(cardName, amount, date, pictureName, picture)

