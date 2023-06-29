package com.example.domain.repo

import com.example.domain.model.local.DomainRoomData
import okhttp3.MultipartBody

/**
 * 2023-02-15
 * pureum
 */
interface RoomRepo {
    suspend fun insertData(list: DomainRoomData)
    suspend fun getAllData():ArrayList<DomainRoomData>
    suspend fun deleteData(date: String) : Int
    suspend fun updateData(
        beforeTime: String,
        cardName: String,
        amount: String,
        storeName: String,
        billSubmitTime: String
    ) : Int

}