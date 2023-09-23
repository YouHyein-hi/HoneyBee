package com.example.data.local.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.local.RoomData
import com.example.domain.util.StringUtil

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
    val memo: String,
    @ColumnInfo
    val picture: String,

)

fun MyEntity.toDomainEntity(): RoomData = RoomData(
    uid = uid,
    billSubmitTime = StringUtil.changeDate(billSubmitTime),
    cardName = cardName,
    storeAmount = amount,
    storeName = pictureName,
    memo = memo,
    file = picture
)