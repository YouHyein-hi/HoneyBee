package com.example.domain.model.remote.send.bill

import android.net.Uri

/**
 * 2023-08-30
 * pureum
 */
data class SendBillData(
    val cardName: String,
    val amount: String,
    var date: String,
    val storeName: String,
    val picture: Uri
)
