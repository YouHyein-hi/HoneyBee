package com.example.receiptcareapp.dto

import android.graphics.Bitmap
import android.net.Uri
import com.example.receiptcareapp.State.ShowType


/**
 * 2023-04-06
 * pureum
 */
data class ShowData(
    var type : ShowType,
    val uid : String,
    val cardName: String,
    val amount: String,
    var date: String,
    val storeName: String,
    val file: Bitmap?
)
