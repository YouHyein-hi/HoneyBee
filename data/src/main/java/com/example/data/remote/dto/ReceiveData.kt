package com.example.data.remote.dto

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val date: LocalDateTime,
    val amount:Int,
    val card:String,
    val picture: MultipartBody.Part
)

fun ReceiveData.toDomainReceiveData() = DomainReceiveData(date, amount, card, picture)