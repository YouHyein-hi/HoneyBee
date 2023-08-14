package com.example.data.remote.model

import androidx.room.IMultiInstanceInvalidationCallback
import com.example.domain.model.receive.DomainReceiveAllData
import java.text.DecimalFormat
import java.text.MessageFormat

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val cardName: String,
    val billId: String,
    val amount: String,
    val billSubmitTime: String,
    val storeName: String,
    var billCheck: Boolean
)

fun ReceiveData.toDomainReceiveData(): DomainReceiveAllData{
    val myAmount = DecimalFormat("#,###").format(amount.toInt()).toString()
    return DomainReceiveAllData(billId, cardName, myAmount, changeDate(billSubmitTime), storeName, billCheck)
}