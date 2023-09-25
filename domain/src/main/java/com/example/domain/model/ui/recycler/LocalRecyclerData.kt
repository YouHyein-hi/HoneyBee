package com.example.domain.model.ui.recycler

import android.net.Uri

/**
 * 2023-08-31
 * pureum
 */
data class LocalRecyclerData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val memo: String,
    val file: Uri
)
