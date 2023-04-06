package com.example.domain.model

import android.net.Uri

/**
 * 2023-02-02
 * pureum
 */
data class DomainReceiveAllData(
    val cardName: String,
    val amount: String,
    var date: String,
    val pictureName: String,
    val picture: String
)

//fun DomainReceiveAllData.toDomainRecyclerViewData(): DomainRecyclerData=
//    DomainRecyclerData(
//        cardName, amount, date, pictureName, picture.toString()
//)

