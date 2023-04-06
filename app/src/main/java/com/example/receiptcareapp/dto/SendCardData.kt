package com.example.receiptcareapp.dto

import okhttp3.MultipartBody

/**
 * 2023-04-06
 * pureum
 */
data class SendCardData(
    var cardName: String,
    var cardAmount: String
)
