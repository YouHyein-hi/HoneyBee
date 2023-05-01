package com.example.data.remote.model

import com.example.domain.model.receive.DomainReceiveCardData
import java.text.DecimalFormat

/**
 * 2023-03-30
 * pureum
 */
data class ReceiveCardData(
    var uid:Long,
    var cardName:String,
    var cardAmount:Int
)

fun ReceiveCardData.toDomainReceiveCardData():DomainReceiveCardData{
    val myCardAmount = DecimalFormat("#,###")
    myCardAmount.format(cardAmount.toString().replace(",","").toInt())
    return DomainReceiveCardData(uid,cardName, myCardAmount.toString())
}
