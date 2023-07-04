package com.example.domain.model.receive

import android.graphics.Bitmap
import android.net.Uri

/**
 * 2023-02-02
 * pureum
 */
data class DomainReceiveAllData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    //청구
//    val file: Bitmap
)

