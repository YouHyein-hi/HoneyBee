package com.example.domain.usecase

import com.example.domain.model.DomainRoomData
import com.example.domain.repo.RoomRepo
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-15
 * pureum
 */
class RoomUseCase(
    private val roomRepo:RoomRepo
) {
    suspend fun insertData(list:DomainRoomData){
        roomRepo.insertData(list)
    }

    suspend fun getAllData(): ArrayList<DomainRoomData>{
        return roomRepo.getAllData()
    }

    suspend fun deleteData(date:String):Int{
        return roomRepo.deleteData(date)
    }
}