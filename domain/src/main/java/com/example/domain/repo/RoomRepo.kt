package com.example.domain.repo

import com.example.domain.model.DomainRoomData
import okhttp3.MultipartBody

/**
 * 2023-02-15
 * pureum
 */
interface RoomRepo {
    suspend fun insertData(list:DomainRoomData)
    suspend fun getAllData():ArrayList<DomainRoomData>
    suspend fun deleteData(id: Long) : Int
}