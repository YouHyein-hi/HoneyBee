package com.example.data.remote.dto

import android.net.Uri
import com.example.domain.model.DomainReceiveAllData
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val id: Long,
    val cardName: String,
    val amount: String,
    val date: String,
    val pictureName: String,
    val picture: String
)

//fun ReceiveData.toDomainReceiveData() = DomainReceiveAllData(cardName, amount, date, pictureName, picture)