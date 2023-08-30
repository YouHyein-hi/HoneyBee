package com.example.domain.usecase.room

import com.example.domain.model.local.RoomData
import com.example.domain.repo.RoomRepository

/**
 * 2023-07-23
 * pureum
 */
class GetRoomDataListUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(): MutableList<RoomData>{
        return roomRepository.getAllData()
    }
}