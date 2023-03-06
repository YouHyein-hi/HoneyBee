package com.example.domain.usecase

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo

/**
 * 2023-02-02
 * pureum
 */
class RetrofitUseCase(
    private val retrofitRepo: RetrofitRepo,
) {
    suspend fun sendDataUseCase(date:String, amount : Int, card:String, picture:ByteArray): DomainSendData {
        return retrofitRepo.sendDataRepo(date,amount, card, picture)
    }

    suspend fun receiveDataUseCase(): DomainReceiveData {
        return retrofitRepo.receiveDataRepo()
    }
}