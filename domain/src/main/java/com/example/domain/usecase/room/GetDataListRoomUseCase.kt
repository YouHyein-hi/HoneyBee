package com.example.domain.usecase.room

import com.example.domain.model.local.DomainRoomData
import com.example.domain.repo.RoomRepository

/**
 * 2023-07-23
 * pureum
 */
class GetDataListRoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(): MutableList<DomainRoomData>{
        return roomRepository.getAllData()
    }
}