package com.example.data.local.entity

import android.graphics.Picture
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainRoomData
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-15
 * pureum
 */

@Entity(tableName = "MyDataTable")
data class MyEntity(
    @PrimaryKey
    val date : String,
    @ColumnInfo
    val cardName: String,
    @ColumnInfo
    val amount: String,
    @ColumnInfo
    val pictureName: String,
    @ColumnInfo
    val picture: String,
)

fun MyEntity.toDomainEntity():DomainRoomData = DomainRoomData(
    date = date,
    cardName = cardName,
    amount = amount,
    pictureName = pictureName,
    picture = picture

)