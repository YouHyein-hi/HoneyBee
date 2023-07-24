package com.example.domain.usecase.card

import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpdateCardData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(domainUpdateCardData: DomainUpdateCardData): DomainServerReponse {
        return cardRepository.updateCardUseCase(domainUpdateCardData)
    }
    // 이 기능은 빼는게 나을지도
}