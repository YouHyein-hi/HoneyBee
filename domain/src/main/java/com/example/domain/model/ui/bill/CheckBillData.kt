package com.example.domain.model.ui.bill

import android.net.Uri

/**
 * 2023-08-31
 * pureum
 */
data class CheckBillData(
    val cardName: String,
    val amount: String,
    val cardAmount: String,
    val storeName: String,
    val date: String,
    val picture: Uri,
)