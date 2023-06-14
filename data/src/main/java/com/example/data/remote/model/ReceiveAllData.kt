package com.example.data.remote.model

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.domain.model.receive.DomainReceiveAllData
import okio.utf8Size
import java.text.DecimalFormat
import java.time.LocalDateTime
import kotlin.math.log

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
)

fun ReceiveData.toDomainReceiveData(): DomainReceiveAllData{
    var myAmount = amount
    Log.e("TAG", "toDomainReceiveData amount: $myAmount", )
    if(amount.toInt()>=1000){
        val format = DecimalFormat("#,###")
        myAmount = format.format(amount.toInt()).toString()
    }
    var myData = date
    if(date.contains("-") && date.contains("T") && date.contains(":")){
        val myList = myData.split("-","T",":")
        Log.e("TAG", "toDomainReceiveData: $myList", )
        if(myList.size == 6)
            myData = "${myList[0]}년 ${myList[1]}월 ${myList[2]}일 ${myList[3]}시 ${myList[4]}분"
    }
    Log.e("TAG", "toDomainReceiveData amount: $myAmount", )

    return DomainReceiveAllData(uid, cardName, myAmount, myData, storeName)
}