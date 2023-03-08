package com.example.data.remote.dto

import com.example.domain.model.DomainSendData
import okhttp3.MultipartBody
import java.time.LocalDateTime


/**
 * 2023-02-02
 * pureum
 */

data class SendData(
    val date:LocalDateTime,
    val amount:Int,
    val card:String,
    val picture:MultipartBody.Part
)


fun SendData.toDomainSendData(): DomainSendData = DomainSendData(date, amount, card, picture)