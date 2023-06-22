package com.example.receiptcareapp.dto

import android.net.Uri
import com.example.receiptcareapp.State.ShowType

/**
 * 2023-06-22
 * pureum
 */
data class RecyclerData(
    var type : ShowType,
    val uid : String,
    val cardName: String,
    val amount: String,
    var billSubmitTime: String,
    val storeName: String,
    var file: Uri?
)
