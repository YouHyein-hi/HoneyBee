package com.example.data.local.entity

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
    val time:LocalDateTime,
    @ColumnInfo
    val amount: Int,
    @ColumnInfo
    val card: String,
    @ColumnInfo
    val picture: MultipartBody.Part?
)

fun MyEntity.toDomainEntity():DomainRoomData = DomainRoomData(time,amount,card,picture)