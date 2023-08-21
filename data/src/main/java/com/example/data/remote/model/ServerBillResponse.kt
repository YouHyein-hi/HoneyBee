package com.example.data.remote.model

import com.example.domain.model.receive.BillData

/**
 * 2023-08-20
 * pureum
 */
data class ServerBillResponse(
    val billId: String,
    val cardName: String,
    val amount: String,
    val billSubmitTime: String,
    val storeName: String,
    var billCheck: Boolean
)

fun ServerBillResponse.toBillData():BillData = BillData(uid = billId, cardName = cardName, storeAmount = amount, date = billSubmitTime, storeName = storeName, billCheck= billCheck)