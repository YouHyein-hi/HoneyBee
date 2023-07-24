//package com.example.domain.usecase
//
//import android.util.Log
//import com.example.domain.model.local.DomainRoomData
//import com.example.domain.repo.RoomRepo
//
///**
// * 2023-02-15
// * pureum
// */
//class RoomUseCase(
//    private val roomRepo:RoomRepo
//) {
//    suspend fun insertData(domainRoomData: DomainRoomData){
//        roomRepo.insertData(domainRoomData)
//    }
//
//    suspend fun getAllData(): ArrayList<DomainRoomData>{
//        return roomRepo.getAllData()
//    }
//
//    suspend fun deleteData(date:String):Int{
//        return roomRepo.deleteData(date)
//    }
//
//    suspend fun updateData(
//        list:DomainRoomData
//    ){
//        Log.e("TAG", "updateData: $list", )
//        roomRepo.updateData(list)
//    }
//}