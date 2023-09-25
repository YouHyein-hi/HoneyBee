package com.example.domain.model.remote.send.bill

import android.net.Uri
import java.time.LocalDateTime

data class SendBillUpdateData(
    val id: Long,
    val cardName: String,
    val storeName: String,
    var billSubmitTime: LocalDateTime,
    val storeAmount: Int,
    val billCheck : Boolean,
    val billMemo: String
)
