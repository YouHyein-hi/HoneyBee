package com.example.domain.usecase.room

import com.example.domain.repo.RoomRepo

/**
 * 2023-07-23
 * pureum
 */
class DeleteDataRoomUseCase(
    private val roomRepo: RoomRepo
) {
    suspend operator fun invoke(date:String):Int{
        return roomRepo.deleteData(date)
    }
}