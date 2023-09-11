package com.example.domain.model.remote.receive.bill

import com.example.domain.model.ui.recycler.ServerRecyclerData

/**
 * 2023-08-20
 * pureum
 */
data class ServerDetailBillData(
    val status: String,
    val message: String,
    val body: DetailBillData?
)


data class DetailBillData(
    val billId: String,
    val cardName: String,
    val billAmount: String,
    val billSubmitTime: String,
    val storeName: String,
    var billCheck: Boolean,
    var writerName: String,
    var writerEmail: String,
    var writeDateTime: String,
    var modifierName: String,
    var modifierEmail: String,
    var modifyDateTime: String
)

