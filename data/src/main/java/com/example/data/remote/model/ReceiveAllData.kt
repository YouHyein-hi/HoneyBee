package com.example.data.remote.model

import android.graphics.BitmapFactory
import android.util.Base64
import com.example.domain.model.receive.DomainReceiveAllData
import okio.utf8Size
import java.text.DecimalFormat
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class ReceiveData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val file: String
)

fun ReceiveData.toDomainReceiveData(): DomainReceiveAllData{
    val myAmount = DecimalFormat("#,###")
    myAmount.format(amount.replace(",","").toInt())

    var myData = date
    if(date.contains("-") && date.contains("T") && date.contains(":")){
        val myList = myData.split("-","T",":")
        if(myList.size == 6)
            myData = "${myData[0]}년 ${myData[1]}월 ${myData[2]}일 ${myData[3]}시 ${myData[4]}분 ${myData[5]}초"
    }

    val gap = file.toByteArray()
    val decode = Base64.decode(gap, Base64.DEFAULT)
    val myBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size)

    return DomainReceiveAllData(uid, cardName, amount, myData, storeName, myBitmap)
}