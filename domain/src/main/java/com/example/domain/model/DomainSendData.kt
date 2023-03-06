package com.example.domain.model

/**
 * 2023-02-02
 * pureum
 */
data class DomainSendData(
    val date:String,
    val amount:Int,
    val card:String,
    val picture:ByteArray
)

