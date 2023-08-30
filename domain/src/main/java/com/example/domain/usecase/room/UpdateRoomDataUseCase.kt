package com.example.domain.usecase.room

import android.util.Log
import com.example.domain.model.local.RoomData
import com.example.domain.repo.RoomRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateRoomDataUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(list: RoomData): Int{
        Log.e("TAG", "updateData: $list", )
        return roomRepository.updateData(list)
    }
}