package com.example.receiptcareapp.dto

import android.net.Uri
import com.example.receiptcareapp.State.ShowType

/**
 * 2023-04-06
 * pureum
 */
data class ShowData(
    var type : ShowType,
    val id : Long,
    val cardName: String,
    val amount: String,
    var date: String,
    val pictureName: String,
    val picture: Uri
)
