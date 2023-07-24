package com.example.domain.usecase.data

import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(domainResendData: DomainUpadateData): DomainServerReponse {
        return generalRepository.updateDataUseCaseRepository(domainResendData)
    }
}