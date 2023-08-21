package com.example.data.repoImpl

import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity
import com.example.data.local.entity.toDomainEntity
import com.example.domain.model.local.DomainRoomData
import com.example.domain.repo.RoomRepository
import javax.inject.Inject

/**
 * 2023-02-15
 * pureum
 */
class RoomRepositoryImpl @Inject constructor(
    private val roomDao: MyDao
    ) : RoomRepository {

    override suspend fun insertData(list: DomainRoomData) {
        val myList = MyEntity(
            billSubmitTime = list.billSubmitTime,
            cardName = list.cardName,
            amount = list.amount,
            pictureName = list.storeName,
            picture = list.file,
            uid = list.uid
        )
        roomDao.insertData(myList)
    }

    override suspend fun getAllData(): MutableList<DomainRoomData> {
        return roomDao.getAllData().map { it.toDomainEntity() }.toMutableList()
    }

    override suspend fun deleteData(date: String): Int {
        return roomDao.deleteData(date = date)
    }

    override suspend fun updateData(list: DomainRoomData):Int {
        return roomDao.updateData(
            MyEntity(
                uid = list.uid,
                billSubmitTime = list.billSubmitTime,
                cardName = list.cardName,
                amount = list.amount,
                pictureName = list.storeName,
                picture = list.file
            )
        )
    }
}