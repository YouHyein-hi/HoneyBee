package com.example.data.remote.model

import com.example.domain.model.receive.DomainReceiveCardData

/**
 * 2023-03-30
 * pureum
 */
data class ReceiveCardData(
    var name:String,
    var amount:Int
)

fun ReceiveCardData.toDomainReceiveCardData() = DomainReceiveCardData(name, amount)
