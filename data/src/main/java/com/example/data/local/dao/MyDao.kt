package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.MyEntity

/**
 * 2023-02-15
 * pureum
 */
@Dao
interface MyDao {
    @Query("Select * From MyDataTable")
    fun getAllData():List<MyEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT) //err
    fun insertData(list:MyEntity)

    @Query("Delete from MyDataTable where time = :time")
    fun deleteData(time:String)
}