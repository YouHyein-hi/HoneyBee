package com.example.domain.model

import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class DomainReceiveData(
    val date: LocalDateTime,
    val amount:Int,
    val card:String,
    val picture: MultipartBody.Part
)
