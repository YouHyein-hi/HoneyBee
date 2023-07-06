package com.example.data.remote.model

import com.example.domain.model.receive.DomainReceiveAllData
import java.text.DecimalFormat

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val billId: String,
    val cardName: String,
    val amount: String,
    val billSubmitTime: String,
    val storeName: String,
    var billCheck: Boolean

)

fun ReceiveData.toDomainReceiveData(): DomainReceiveAllData{
    val myAmount = DecimalFormat("#,###").format(amount.toInt()).toString()

    var myData = billSubmitTime
    if(billSubmitTime.contains("-") && billSubmitTime.contains("T") && billSubmitTime.contains(":")){
        val myList = myData.split("-","T",":")
        if(myList.size == 6)
            myData = "${myList[0]}년 ${myList[1]}월 ${myList[2]}일 ${myList[3]}시 ${myList[4]}분"
    }

    return DomainReceiveAllData(billId, cardName, myAmount, myData, storeName, billCheck)
}