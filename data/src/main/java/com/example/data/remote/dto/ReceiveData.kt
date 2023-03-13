package com.example.data.remote.dto

import android.net.Uri
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val cardName: String,
    val amount: String,
    val date: LocalDateTime,
    val pictureName: String,
    val picture: Uri
)

fun ReceiveData.toDomainReceiveData() = DomainReceiveData(cardName, amount, date, pictureName, picture)