package com.example.domain.usecase

import com.example.domain.model.*
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(domainSendData:DomainSendData): String {
        return retrofitRepo.sendDataRepo(domainSendData)
    }

    suspend fun receiveDataUseCase(): MutableList<DomainReceiveAllData> {
        return retrofitRepo.receiveDataRepo()
    }

    suspend fun deleteServerData(id:Long): String {
        return retrofitRepo.deleteServerData(id)
    }

    suspend fun sendCardDataUseCase(domainSendCardData: DomainSendCardData) : String{
        return retrofitRepo.sendCardDataRepo(domainSendCardData)
    }

    suspend fun receiveCardDataUseCase() : MutableList<DomainReceiveCardData>{
        return retrofitRepo.receiveCardDataRepo()
    }

    suspend fun deleteCardDataUseCase(id:Long):String {
        return retrofitRepo.deleteCardDataRepo(id)
    }

    suspend fun resendDataUseCase(domainResendData: DomainResendData):String {
        return retrofitRepo.resendDataRepo(domainResendData)
    }

    suspend fun resendCardDataUseCase(domainResendCardData: DomainResendCardData):String {
        return retrofitRepo.resendCardDataRepo(domainResendCardData)
    }
}