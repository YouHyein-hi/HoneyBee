package com.example.domain.model.send

import okhttp3.MultipartBody

/**
 * 2023-04-06
 * pureum
 */
data class DomainSendCardData(
    var cardName:String,
    var cardAmount:Int
)
