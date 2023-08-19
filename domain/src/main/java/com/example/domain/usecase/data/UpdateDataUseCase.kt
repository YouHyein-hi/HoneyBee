package com.example.domain.usecase.data

import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(domainResendData: DomainUpadateData): DomainServerResponse {
        return generalRepository.updateDataUseCaseRepository(domainResendData)
    }
}