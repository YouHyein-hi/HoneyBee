package com.example.data.local.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.local.DomainRoomData

/**
 * 2023-02-15
 * pureum
 */

@Entity(tableName = "MyDataTable")
data class MyEntity(
    @PrimaryKey
    val uid : String,
    @ColumnInfo
    val billSubmitTime : String,
    @ColumnInfo
    val cardName: String,
    @ColumnInfo
    val amount: String,
    @ColumnInfo
    val pictureName: String,
    @ColumnInfo
    val picture: String,

)

fun MyEntity.toDomainEntity(): DomainRoomData = DomainRoomData(
    uid = uid,
    billSubmitTime = billSubmitTime,
    cardName = cardName,
    amount = amount,
    storeName = pictureName,
    file = picture
)