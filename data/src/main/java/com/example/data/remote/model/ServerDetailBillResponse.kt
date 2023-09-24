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
    var billMemo: String,
    var writerName: String,
    var writeDateTime: String,
    var modifierName: String,
    var modifyDateTime: String


)

fun ServerDetailBillResponse.toDetailBillData(): DetailBillData = DetailBillData(
    billId = billId,
    cardName = cardName,
    billAmount = billAmount,
    billSubmitTime = billSubmitTime,
    storeName = storeName,
    billCheck = billCheck,
    billMemo = billMemo,
    writerName = writerName,
    writeDateTime = writeDateTime,
    modifierName = modifierName,
    modifyDateTime = modifyDateTime
)