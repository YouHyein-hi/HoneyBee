package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainRoomData

/**
 * 2023-02-15
 * pureum
 */

@Entity(tableName = "MyDataTable")
data class MyEntity(
    @PrimaryKey
    val time:String,
    @ColumnInfo
    val card: String,
    @ColumnInfo
    val picture: ByteArray,
)

fun MyEntity.toDomainEntity():DomainRoomData = DomainRoomData(time,card,picture)