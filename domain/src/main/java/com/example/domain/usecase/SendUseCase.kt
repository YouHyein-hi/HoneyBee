package com.example.domain.usecase

import com.example.domain.model.DomainSendData
import com.example.domain.repo.SendRepo

/**
 * 2023-02-02
 * pureum
 */
class SendUseCase(
    private val repository : SendRepo) {
    suspend operator fun invoke():List<DomainSendData>{
        return repository.sendDataRepo()
    }
}