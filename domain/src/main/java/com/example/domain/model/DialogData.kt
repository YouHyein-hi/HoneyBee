package com.example.domain.model

import android.net.Uri

/**
 * 2023-06-15
 * pureum
 */
data class DialogData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val file: Uri?
)
