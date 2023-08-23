package com.example.domain.usecase.bill

import com.example.domain.model.receive.bill.ServerBillData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class GetDataListUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): ServerBillData {
        return generalRepository.getDataListRepository()
    }
}