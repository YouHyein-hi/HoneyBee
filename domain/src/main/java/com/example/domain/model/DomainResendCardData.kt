package com.example.domain.model

import okhttp3.MultipartBody

/**
 * 2023-04-06
 * pureum
 */
data class DomainResendCardData(
    var cardName: String,
    var cardAmount: Int
)
