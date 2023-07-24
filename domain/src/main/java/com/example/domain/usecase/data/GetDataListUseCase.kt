package com.example.domain.usecase.data

import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class GetDataListUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): MutableList<DomainReceiveAllData> {
        return generalRepository.getDataListRepository()
    }
}