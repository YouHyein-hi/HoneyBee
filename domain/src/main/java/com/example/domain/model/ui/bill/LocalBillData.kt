package com.example.domain.model.ui.bill

import android.net.Uri

/**
 * 2023-08-21
 * pureum
 */
data class LocalBillData(
    val uid: String,
    val cardName: String,
    val storeAmount: String,
    val storeName: String,
    var date: String,
    val picture: Uri,
)
