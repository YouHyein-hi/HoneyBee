package com.example.domain.usecase

import com.example.domain.model.DomainReceiveData
import com.example.domain.repo.ReceiveRepo

/**
 * 2023-02-02
 * pureum
 */
class ReceiveUseCase (
    private val repository: ReceiveRepo){
    suspend operator fun invoke():DomainReceiveData{
        return repository.receiveDataRepo()
    }
}