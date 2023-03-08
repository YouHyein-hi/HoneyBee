package com.example.domain.usecase

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(date: LocalDateTime, amount : Int, card:String, picture: MultipartBody.Part): DomainSendData {
        return retrofitRepo.sendDataRepo(date,amount, card, picture)
    }

    suspend fun receiveDataUseCase(): DomainReceiveData {
        return retrofitRepo.receiveDataRepo()
    }
}