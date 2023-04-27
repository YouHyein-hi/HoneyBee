package com.example.data.remote.model

import com.example.domain.model.receive.DomainReceiveCardData

/**
 * 2023-03-30
 * pureum
 */
data class ReceiveCardData(
    var uid:Long,
    var cardName:String,
    var cardAmount:Int
)

fun ReceiveCardData.toDomainReceiveCardData() = DomainReceiveCardData(uid,cardName, cardAmount)
