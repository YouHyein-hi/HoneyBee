package com.example.data.remote.model

import com.example.domain.model.remote.receive.bill.BillData

/**
 * 2023-08-20
 * pureum
 */
data class ServerBillResponse(
    val billId: String,
    val cardName: String,
    val billAmount: String,
    val billSubmitTime: String,
    val storeName: String,
    var billCheck: Boolean,
    var writerName: String,
    var writerEmail: String
)

fun ServerBillResponse.toBillData(): BillData = BillData(uid = billId, cardName = cardName, storeAmount = billAmount, date = billSubmitTime, storeName = storeName, billCheck= billCheck)