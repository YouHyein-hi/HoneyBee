package com.example.data.repoImpl

import com.example.data.local.dao.MyDao
import com.example.data.local.entity.MyEntity
import com.example.data.local.entity.toDomainEntity
import com.example.domain.model.local.RoomData
import com.example.domain.repo.RoomRepository
import javax.inject.Inject

/**
 * 2023-02-15
 * pureum
 */
class RoomRepositoryImpl @Inject constructor(
    private val roomDao: MyDao
) : RoomRepository {

    override suspend fun insertData(list: RoomData) {
        roomDao.insertData(
            MyEntity(
                billSubmitTime = list.billSubmitTime,
                cardName = list.cardName,
                amount = list.storeAmount,
                pictureName = list.storeName,
                picture = list.file,
                memo = list.memo,
                uid = list.uid
            )
        )
    }

    override suspend fun getAllData(): MutableList<RoomData> =
        roomDao.getAllData().map { it.toDomainEntity() }.toMutableList()


    override suspend fun deleteData(date: String): Int =
        roomDao.deleteData(date = date)


    override suspend fun updateData(list: RoomData): Int =
        roomDao.updateData(
            MyEntity(
                uid = list.uid,
                billSubmitTime = list.billSubmitTime,
                cardName = list.cardName,
                amount = list.storeAmount,
                pictureName = list.storeName,
                memo = list.memo,
                picture = list.file
            )
        )
}