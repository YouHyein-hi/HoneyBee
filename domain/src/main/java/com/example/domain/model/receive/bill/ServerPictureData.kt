package com.example.domain.model.receive.bill

import android.graphics.Bitmap

/**
 * 2023-08-20
 * pureum
 */
data class ServerPictureData(
    val status: String,
    val message: String,
    val picture: Bitmap
)
