package com.example.domain.model.receive

/**
 * 2023-08-20
 * pureum
 */
data class BillData(
    val uid: String,
    val cardName: String,
    val storeAmount: String,
    val date: String,
    val storeName: String,
    var billCheck: Boolean
)