package com.example.domain.usecase

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(cardName:MultipartBody.Part, amount:MultipartBody.Part, pictureName:MultipartBody.Part, date:MultipartBody.Part, picture:MultipartBody.Part): DomainSendData {
        return retrofitRepo.sendDataRepo(card = cardName, amount = amount, pictureName=pictureName, date = date, picture = picture)
    }

    suspend fun receiveDataUseCase(): DomainReceiveData {
        return retrofitRepo.receiveDataRepo()
    }

    suspend fun test(): String {
        return retrofitRepo.test()
    }
}