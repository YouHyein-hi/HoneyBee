package com.example.domain.model.ui.recycler

import android.net.Uri
import com.example.domain.model.ui.type.ShowType

/**
 * 2023-06-22
 * pureum
 */
data class RecyclerData(
    var type : ShowType,
    val uid : String,
    val cardName: String,
    val amount: String,
    var date: String,
    val storeName: String,
    val memo: String,
    var file: Uri?
)
