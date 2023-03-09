package com.example.domain.model

import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-15
 * pureum
 */
data class DomainRoomData(
    val date: LocalDateTime,
    val amount:Int,
    val card:String,
    val picture: MultipartBody.Part?
)

//fun DomainRoomData.toMyEntity(): = DomainRoomData(time, card, picture)

