package com.example.domain.usecase.room

import com.example.domain.model.local.RoomData
import com.example.domain.repo.RoomRepository

/**
 * 2023-07-23
 * pureum
 */
class InsertRoomDataUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(insertRoomData: RoomData){
        roomRepository.insertData(insertRoomData)
    }
}