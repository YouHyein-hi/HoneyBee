package com.example.domain.usecase

import com.example.domain.model.DomainRoomData
import com.example.domain.repo.RoomRepo

/**
 * 2023-02-15
 * pureum
 */
class RoomUseCase(
    private val roomRepo:RoomRepo
) {
    suspend fun insertData(domainRoomData:DomainRoomData){
        roomRepo.insertData(domainRoomData)
    }

    suspend fun getAllData(): ArrayList<DomainRoomData>{
        return roomRepo.getAllData()
    }

    suspend fun deleteData(id:Long):Int{
        return roomRepo.deleteData(id)
    }
}