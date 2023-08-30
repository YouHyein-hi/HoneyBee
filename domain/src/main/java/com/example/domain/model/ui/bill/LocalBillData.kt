package com.example.domain.model.ui.bill

import android.net.Uri

/**
 * 2023-08-21
 * pureum
 */
data class LocalBillData(
    val uid: String,
    val cardName: String,
    val amount: String,
    var billSubmitTime: String,
    val storeName: String,
    val picture: Uri,
)
