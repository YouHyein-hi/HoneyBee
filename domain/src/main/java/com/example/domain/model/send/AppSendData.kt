package com.example.domain.model.send

import android.net.Uri

/**
 * 2023-04-24
 * pureum
 */
data class AppSendData(
    val cardName: String,
    val amount: String,
    var billSubmitTime: String,
    val storeName: String,
    val picture: Uri
)


