package com.example.domain.model.ui.recycler

/**
 * 2023-08-31
 * pureum
 */
data class ServerRecyclerData(
    val uid: String?,
    val cardName: String,
    val storeAmount: String,
    val date: String,
    val storeName: String,
    var billCheck: Boolean
)
