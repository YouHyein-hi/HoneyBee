package com.example.domain.usecase

import com.example.domain.model.DomainReceiveCardData
import com.example.domain.model.DomainReceiveAllData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(cardName:MultipartBody.Part, amount:MultipartBody.Part, pictureName:MultipartBody.Part, date:MultipartBody.Part, picture:MultipartBody.Part): String {
        return retrofitRepo.sendDataRepo(card = cardName, amount = amount, pictureName=pictureName, date = date, picture = picture)
    }

    suspend fun receiveDataUseCase(): MutableList<DomainReceiveAllData> {
        return retrofitRepo.receiveDataRepo()
    }

    suspend fun deleteServerData(date:String): String {
        return retrofitRepo.deleteServerData(date)
    }

    suspend fun sendCardDataUseCase(cardName:MultipartBody.Part, amount:MultipartBody.Part) : String{
        return retrofitRepo.sendCardDataRepo(card = cardName, amount = amount)
    }

    suspend fun receiveCardDataUseCase() : MutableList<DomainReceiveCardData>{
        return retrofitRepo.receiveCardDataRepo()
    }

    suspend fun deleteCardDataUseCase():String {
        return retrofitRepo.deleteCardDataRepo()
    }

    suspend fun myTest(file:MultipartBody.Part):String {
        return retrofitRepo.myTest(file)
    }
}