package com.example.domain.usecase.card

import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class InsertCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(domainSendCardData: DomainSendCardData) : DomainServerResponse {
        return cardRepository.insertCardUseCase(domainSendCardData)
    }
}