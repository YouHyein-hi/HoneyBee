package com.example.data.repoImpl

import androidx.room.TypeConverters
import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity
import com.example.domain.model.DomainRoomData
import com.example.domain.repo.RoomRepo
import javax.inject.Inject

/**
 * 2023-02-15
 * pureum
 */
class RoomRepoImpl @Inject constructor(private val roomDao: MyDao):RoomRepo{

    override suspend fun insertData(list:DomainRoomData) {
        val myList = MyEntity(list.date, list.amount, list.card, list.picture)
        roomDao.insertData(myList)
    }

    override suspend fun getAllData(): ArrayList<DomainRoomData> {
        val myList = arrayListOf<DomainRoomData>()
        roomDao.getAllData().map { myList.add(DomainRoomData(it.time, it.amount, it.card, it.picture)) }
        return myList
    }

    override suspend fun deleteData(time: String) {
        roomDao.deleteData(time)
    }
}