package com.example.data.remote.dto

import com.example.domain.model.DomainSendData


/**
 * 2023-02-02
 * pureum
 */

data class SendData(
    val card:String,
    val date:String,
    val picture:ByteArray
)


fun SendData.toDomainSendData(): DomainSendData = DomainSendData("?")