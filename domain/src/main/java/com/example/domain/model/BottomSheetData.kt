package com.example.domain.model

import android.net.Uri

/**
 * 2023-07-25
 * pureum
 */
data class BottomSheetData(
    val cardName: String,
    val amount: String,
    val cardAmount: String,
    val storeName: String,
    val date: String,
    val picture: Uri,
)
