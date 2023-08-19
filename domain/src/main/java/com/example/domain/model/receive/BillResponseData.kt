package com.example.domain.model.receive

/**
 * 2023-08-20
 * pureum
 */
data class BillResponseData(
    val status: String,
    val message: String,
    val body: List<BillData>?
)

data class BillData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    var billCheck: Boolean
)

data class PictureData(
    val body: String,
)
