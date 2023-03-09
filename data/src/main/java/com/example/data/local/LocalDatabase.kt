package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity
import com.example.data.repoImpl.RoomRepoImpl
import java.time.LocalDateTime

/**
 * 2023-02-15
 * pureum
 */
@Database(entities = [MyEntity::class], version = 1)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
        MultipartBodyConverter::class
    ]
)
abstract class LocalDatabase :RoomDatabase(){
    abstract fun myDatabase():MyDao
}