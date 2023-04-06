package com.example.receiptcareapp.dto

import android.net.Uri

/**
 * 2023-04-06
 * pureum
 */
data class SendData(
    val cardName: String,
    val amount: String,
    var date: String,
    val storeName: String,
    val picture: Uri
)
