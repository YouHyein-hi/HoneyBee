package com.example.domain.model.remote.send.bill

import okhttp3.MultipartBody

/**
 * 2023-08-30
 * pureum
 */
data class SendBillData(
    val cardName: MultipartBody.Part,
    val amount: MultipartBody.Part,
    var date: MultipartBody.Part,
    val storeName: MultipartBody.Part,
    val picture: MultipartBody.Part
)
