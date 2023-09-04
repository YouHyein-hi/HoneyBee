package com.example.domain.usecase.bill

import com.example.domain.model.remote.receive.bill.ServerStoreData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-08-21
 * pureum
 */
class GetStoreListUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): ServerStoreData {
        return generalRepository.getStoreListRepository()
    }
}