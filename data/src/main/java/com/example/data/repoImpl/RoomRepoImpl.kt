package com.example.data.repoImpl

import android.util.Log
import androidx.room.TypeConverters
import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity
import com.example.domain.model.DomainRoomData
import com.example.domain.repo.RoomRepo
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * 2023-02-15
 * pureum
 */
class RoomRepoImpl @Inject constructor(private val roomDao: MyDao):RoomRepo{

    override suspend fun insertData(list:DomainRoomData) {
        val myList = MyEntity(
            date = list.date,
            cardName = list.cardName,
            amount = list.amount,
            pictureName = list.pictureName,
            picture = list.picture
        )
        roomDao.insertData(myList)
    }

    override suspend fun getAllData(): ArrayList<DomainRoomData> {
        val myList = arrayListOf<DomainRoomData>()
        roomDao.getAllData().map { myList.add(DomainRoomData(
            cardName = it.cardName,
            amount = it.amount,
            date = it.date,
            pictureName = it.pictureName,
            picture = it.picture

        )) }
        return myList
    }

    override suspend fun deleteData(date: String) {
        roomDao.deleteData(date)
    }
}