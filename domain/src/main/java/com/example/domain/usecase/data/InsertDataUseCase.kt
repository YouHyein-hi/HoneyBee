package com.example.domain.usecase.data

import com.example.domain.model.send.DomainSendData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class InsertDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(domainSendData: DomainSendData): String {
        return generalRepository.insertDataRepository(domainSendData)
    }
}