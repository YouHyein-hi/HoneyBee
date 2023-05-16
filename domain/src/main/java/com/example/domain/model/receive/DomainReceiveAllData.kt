package com.example.domain.model.receive

import android.graphics.Bitmap
import android.net.Uri

/**
 * 2023-02-02
 * pureum
 */
//data class DomainReceiveAllData(
//    val cardName: String,
//    val amount: String,
//    var date: String,
//    val pictureName: String,
//    val picture: String
//)

data class DomainReceiveAllData(
    val uid: String,
    val cardName: String,
    val amount: String,
    val date: String,
    val storeName: String,
    val file: Bitmap
)

//fun DomainReceiveAllData.toDomainRecyclerViewData(): DomainRecyclerData=
//    DomainRecyclerData(
//        cardName, amount, date, pictureName, picture.toString()
//)

