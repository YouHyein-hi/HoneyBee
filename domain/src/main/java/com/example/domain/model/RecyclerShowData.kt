package com.example.domain.model

import android.graphics.Bitmap
import android.net.Uri

/**
 * 2023-05-09
 * pureum
 */
data class RecyclerShowData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val file: Uri
)
