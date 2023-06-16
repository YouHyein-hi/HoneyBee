package com.example.domain.usecase

import android.graphics.Bitmap
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainResendAllData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.model.send.DomainSendData
import com.example.domain.repo.RetrofitRepo

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(domainSendData: DomainSendData): String {
        return retrofitRepo.sendDataRepo(domainSendData)
    }

    suspend fun receiveDataUseCase(): MutableList<DomainReceiveAllData> {
        return retrofitRepo.receiveDataRepo()
    }

    suspend fun receiveCardDataUseCase() : MutableList<DomainReceiveCardData>{
        return retrofitRepo.receiveCardDataRepo()
    }

    suspend fun receivePictureDataUseCase(uid:String) : Bitmap{
        return retrofitRepo.receivePictureDataRepo(uid)
    }

    suspend fun deleteServerData(id:Long): String {
        return retrofitRepo.deleteServerData(id)
    }

    suspend fun sendCardDataUseCase(domainSendCardData: DomainSendCardData) : String{
        return retrofitRepo.sendCardDataRepo(domainSendCardData)
    }



    suspend fun deleteCardDataUseCase(id:Long):String {
        return retrofitRepo.deleteCardDataRepo(id)
    }

    suspend fun updateDataUseCase(domainResendData: DomainResendAllData):String {
        return retrofitRepo.updateDataRepo(domainResendData)
    }

//    suspend fun resendCardDataUseCase(domainResendCardData: DomainResendCardData):String {
//        return retrofitRepo.resendCardDataRepo(domainResendCardData)
//    }
}