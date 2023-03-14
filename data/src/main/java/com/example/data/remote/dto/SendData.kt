package com.example.data.remote.dto

import com.example.domain.model.DomainSendData
import okhttp3.MultipartBody
import okhttp3.Response
import java.time.LocalDateTime


/**
 * 2023-02-02
 * pureum
 */

data class SendData(
    val response: String,
)


//fun SendData.toDomainSendData(): DomainSendData = DomainSendData(response = response)