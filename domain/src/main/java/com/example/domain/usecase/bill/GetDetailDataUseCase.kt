package com.example.domain.usecase.bill

import com.example.domain.model.remote.receive.bill.ServerDetailBillData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class GetDetailDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(id: String): ServerDetailBillData =
        generalRepository.getDetailDataRepository(id)
}