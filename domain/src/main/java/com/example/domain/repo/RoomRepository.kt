package com.example.domain.repo

import com.example.domain.model.local.DomainRoomData
import okhttp3.MultipartBody

/**
 * 2023-02-15
 * pureum
 */
interface RoomRepository {
    suspend fun insertData(list: DomainRoomData)
    suspend fun getAllData():MutableList<DomainRoomData>
    suspend fun deleteData(date: String) : Int
    suspend fun updateData(list: DomainRoomData)

}