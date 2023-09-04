package com.example.domain.model.remote.receive.bill

import com.example.domain.model.ui.recycler.ServerRecyclerData

/**
 * 2023-08-20
 * pureum
 */
data class ServerBillData(
    val status: String,
    val message: String,
    val body: List<BillData>?
)

data class BillData(
    val uid: String?,
    val cardName: String,
    val storeAmount: String,
    val date: String,
    val storeName: String,
    var billCheck: Boolean
)

fun BillData.toServerRecyclerData(): ServerRecyclerData = ServerRecyclerData(
        uid, cardName, storeAmount, date, storeName, billCheck
    )

