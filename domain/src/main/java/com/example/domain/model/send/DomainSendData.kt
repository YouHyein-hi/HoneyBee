package com.example.domain.model.send

import okhttp3.MultipartBody
import okhttp3.Response
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class DomainSendData(
    val cardName: MultipartBody.Part,
    val amount: MultipartBody.Part,
    var date: MultipartBody.Part,
    val storeName: MultipartBody.Part,
    val picture: MultipartBody.Part
)

