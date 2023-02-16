package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity

/**
 * 2023-02-15
 * pureum
 */
@Database(entities = [MyEntity::class], version = 1)
abstract class LocalDatabase :RoomDatabase(){
    abstract fun myDatabase():MyDao
}