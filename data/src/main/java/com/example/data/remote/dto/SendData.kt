package com.example.data.remote.dto

import com.example.domain.model.DomainSendData


/**
 * 2023-02-02
 * pureum
 */

data class SendData(
    val date:String,
    val amount:Int,
    val card:String,
    val picture:ByteArray
)


fun SendData.toDomainSendData(): DomainSendData = DomainSendData(date, amount, card, picture)