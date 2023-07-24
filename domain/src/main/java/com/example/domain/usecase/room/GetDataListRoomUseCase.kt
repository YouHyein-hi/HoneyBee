package com.example.domain.usecase.room

import com.example.domain.model.local.DomainRoomData
import com.example.domain.repo.RoomRepo

/**
 * 2023-07-23
 * pureum
 */
class GetDataListRoomUseCase(
    private val roomRepo: RoomRepo
) {
    suspend operator fun invoke(): ArrayList<DomainRoomData>{
        return roomRepo.getAllData()
    }
}