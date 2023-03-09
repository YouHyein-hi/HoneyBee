package com.example.domain.usecase

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(card:String, amount:Int, pictureName:String, date:LocalDateTime, bill:MultipartBody.Part): DomainSendData {
        return retrofitRepo.sendDataRepo(card = card, amount = amount, pictureName="ㄹㅇㄴㄹㄴㅇㄹㄴㅁ", date = date, bill = bill)
    }

    suspend fun receiveDataUseCase(): DomainReceiveData {
        return retrofitRepo.receiveDataRepo()
    }

    suspend fun test(): String {
        return retrofitRepo.test()
    }
}