package com.example.domain.usecase.room

import com.example.domain.repo.RoomRepository

/**
 * 2023-07-23
 * pureum
 */
class DeleteRoomDataUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(date:String):Int{
        return roomRepository.deleteData(date)
    }
}