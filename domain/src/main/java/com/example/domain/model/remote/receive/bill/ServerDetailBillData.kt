package com.example.domain.model.remote.receive.bill

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
    var billMemo: String,
    var writerName: String,
    var writeDateTime: String,
    var modifierName: String,
    var modifyDateTime: String
)

