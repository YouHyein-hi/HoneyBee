package com.example.domain.repo

import com.example.domain.model.local.RoomData
/**
 * 2023-02-15
 * pureum
 */
interface RoomRepository {
    suspend fun insertData(list: RoomData)
    suspend fun getAllData():MutableList<RoomData>
    suspend fun deleteData(date: String) : Int
    suspend fun updateData(list: RoomData): Int
}