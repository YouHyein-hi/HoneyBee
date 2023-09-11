package com.example.data.remote.model

import com.example.domain.model.remote.receive.bill.DetailBillData

/**
 * 2023-08-20
 * pureum
 */
data class ServerDetailBillResponse(
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

fun ServerDetailBillResponse.toDetailBillData(): DetailBillData = DetailBillData(billId = billId, cardName = cardName, billAmount = billAmount, billSubmitTime = billSubmitTime, storeName = storeName, billCheck= billCheck,
writerName = writerName, writerEmail = writerEmail, writeDateTime = writeDateTime, modifierName = modifierName, modifierEmail = modifierEmail, modifyDateTime = modifyDateTime)