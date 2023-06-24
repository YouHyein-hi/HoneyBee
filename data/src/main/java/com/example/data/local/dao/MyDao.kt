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
    fun getAllData(): List<MyEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT) //err
    fun insertData(list: MyEntity)

    @Query("Delete from MyDataTable where billSubmitTime = :date")
    fun deleteData(date: String): Int

    @Query("Update MyDataTable SET cardName = :cardName And amount = :amount And pictureName = :pictureName And billSubmitTime = :presentTime Where billSubmitTime = :beforeTime")
    fun updateData(
        beforeTime: String,
        cardName: String,
        amount: String,
        pictureName: String,
        presentTime: String
    ): Int
}