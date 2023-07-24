//package com.example.domain.usecase
//
//import android.graphics.Bitmap
//import android.util.Log
//import com.example.domain.model.receive.*
//import com.example.domain.model.send.DomainSendCardData
//import com.example.domain.model.send.DomainSendData
//import com.example.domain.repo.RetrofitRepo
//
///**
// * 2023-02-02
// * pureum
// */
//class RetrofitUseCase(
//    private val retrofitRepo: RetrofitRepo,
//) {
//    suspend fun sendDataUseCase(domainSendData: DomainSendData): String {
//        return retrofitRepo.sendDataRepo(domainSendData)
//    }
//
//    suspend fun receiveDataUseCase(): MutableList<DomainReceiveAllData> {
//        return retrofitRepo.receiveDataRepo()
//    }
//
//    suspend fun receiveCardDataUseCase() : MutableList<DomainReceiveCardData>{
//        return retrofitRepo.receiveCardDataRepo()
//    }
//
//    suspend fun receivePictureDataUseCase(uid:String) : Bitmap{
//        return retrofitRepo.receivePictureDataRepo(uid)
//    }
//
//    suspend fun deleteServerData(id:Long): String {
//        return retrofitRepo.deleteServerData(id)
//    }
//
//    suspend fun updateDataUseCase(domainResendData: DomainUpadateData):DomainServerReponse {
//        return retrofitRepo.updateDataRepo(domainResendData)
//    }
//
//    suspend fun sendCardDataUseCase(domainSendCardData: DomainSendCardData) : String{
//        return retrofitRepo.sendCardDataRepo(domainSendCardData)
//    }
//
//    suspend fun updateCardDateUseCase(domainUpdateCardData: DomainUpdateCardData) : DomainServerReponse{
//        Log.e("TAG", "updateCardDateUseCase: ", )
//        return retrofitRepo.updateCardDataRepo(domainUpdateCardData)
//    }
//
//    suspend fun requestLoginUseCase(email:String, password:String):DomainServerReponse{
//        return retrofitRepo.requestLogin(email = email, password = password)
//    }
//}