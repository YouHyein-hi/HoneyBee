package com.example.domain.usecase.room

import android.util.Log
import com.example.domain.model.local.DomainRoomData
import com.example.domain.repo.RoomRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateRoomData(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(
        list: DomainRoomData
    ){
        Log.e("TAG", "updateData: $list", )
        roomRepository.updateData(list)
    }
}