package com.example.data.remote.model

import android.util.Log
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
    Log.e("TAG", "toDomainReceiveCardData: $cardAmount", )
    val myCardAmount = DecimalFormat("#,###")
    val newCardAmount = myCardAmount.format(cardAmount.toString().toInt())
    Log.e("TAG", "toDomainReceiveCardData: $newCardAmount", )
    return DomainReceiveCardData(uid,cardName, newCardAmount)
}
