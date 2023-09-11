package com.example.domain.model.ui.bill

import android.net.Uri

/**
 * 2023-08-31
 * pureum
 */
data class CheckBillData(
    val cardAmount: String,
    val cardName: String,
    val storeAmount: String,
    val storeName: String,
    val date: String,
    val picture: Uri,
)