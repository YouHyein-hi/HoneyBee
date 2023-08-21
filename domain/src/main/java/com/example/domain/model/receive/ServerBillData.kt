package com.example.domain.model.receive

/**
 * 2023-08-20
 * pureum
 */
data class ServerBillData(
    val status: String,
    val message: String,
    val body: List<BillData>?
)



