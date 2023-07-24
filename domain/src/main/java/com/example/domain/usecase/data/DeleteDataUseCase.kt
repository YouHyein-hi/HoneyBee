package com.example.domain.usecase.data

import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class DeleteDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(id:Long): String {
        return generalRepository.deleteDataRepository(id)
    }
}