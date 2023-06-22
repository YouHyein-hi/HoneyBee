package com.example.data.repoImpl

import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity
import com.example.domain.model.local.DomainRoomData
import com.example.domain.repo.RoomRepo
import javax.inject.Inject

/**
 * 2023-02-15
 * pureum
 */
class RoomRepoImpl @Inject constructor(private val roomDao: MyDao):RoomRepo{

    override suspend fun insertData(list: DomainRoomData) {
        val myList = MyEntity(
            date = list.billSubmitTime,
            cardName = list.cardName,
            amount = list.amount,
            pictureName = list.storeName,
            picture = list.file,
            uid = list.uid
        )
        roomDao.insertData(myList)
    }

    override suspend fun getAllData(): ArrayList<DomainRoomData> {
        val myList = arrayListOf<DomainRoomData>()
        roomDao.getAllData().map { myList.add(DomainRoomData(
            cardName = it.cardName,
            amount = it.amount,
            billSubmitTime = it.date,
            storeName = it.pictureName,
            file = it.picture,
            uid = it.uid
        )) }
        return myList
    }

    override suspend fun deleteData(date:String):Int {
        return roomDao.deleteData(date = date)
    }
}