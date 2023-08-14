package com.example.data.remote.model

import android.util.Log
import com.example.domain.model.receive.DomainReceiveCardData
import java.text.DecimalFormat

/**
 * 2023-03-30
 * pureum
 */
data class ReceiveCardData(
    var billcardId:Long,
    var cardName:String,
    var cardAmount:Int,
    var billCheckDate:String
)

fun ReceiveCardData.toDomainReceiveCardData():DomainReceiveCardData{
    Log.e("TAG", "toDomainReceiveCardData: uid $billcardId", )
    Log.e("TAG", "toDomainReceiveCardData: cardAmount $cardAmount", )
    val myCardAmount = DecimalFormat("#,###")
    val newCardAmount = myCardAmount.format(cardAmount.toString().toInt())
    Log.e("TAG", "toDomainReceiveCardData: newCarAmount $newCardAmount", )
    return DomainReceiveCardData(billcardId,cardName, newCardAmount, billCheckDate)
}
