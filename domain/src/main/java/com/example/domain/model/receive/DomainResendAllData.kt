package com.example.domain.model.receive

import okhttp3.MultipartBody

/**
 * 2023-04-06
 * pureum
 */
data class DomainResendAllData(
    val id : MultipartBody.Part,
    val cardName: MultipartBody.Part,
    val amount: MultipartBody.Part,
    var date: MultipartBody.Part,
    val storeName: MultipartBody.Part,
    val picture: MultipartBody.Part
)
