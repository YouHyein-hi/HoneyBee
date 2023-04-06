package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.MyEntity
import okhttp3.MultipartBody

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

    @Query("Delete from MyDataTable where date = :date")
    fun deleteData(date:Long):Int
}