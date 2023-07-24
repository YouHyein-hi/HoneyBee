package com.example.domain.usecase.room

import android.util.Log
import com.example.domain.model.local.DomainRoomData
import com.example.domain.repo.RoomRepo

/**
 * 2023-07-23
 * pureum
 */
class UpdateRoomData(
    private val roomRepo: RoomRepo
) {
    suspend operator fun invoke(
        list: DomainRoomData
    ){
        Log.e("TAG", "updateData: $list", )
        roomRepo.updateData(list)
    }
}