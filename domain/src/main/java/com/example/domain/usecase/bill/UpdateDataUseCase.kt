package com.example.domain.usecase.bill

import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.receive.ServerUidData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(domainResendData: DomainUpadateData): ServerUidData {
        return generalRepository.updateDataUseCaseRepository(domainResendData)
    }
}