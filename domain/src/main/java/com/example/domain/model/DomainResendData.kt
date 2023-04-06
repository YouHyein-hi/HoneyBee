package com.example.domain.model

import okhttp3.MultipartBody

/**
 * 2023-04-06
 * pureum
 */
data class DomainResendData(
    val id : MultipartBody.Part,
    val cardName: MultipartBody.Part,
    val amount: MultipartBody.Part,
    var date: MultipartBody.Part,
    val storeName: MultipartBody.Part,
    val picture: MultipartBody.Part
)
