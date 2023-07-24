package com.example.domain.usecase.card

import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class GetCardListUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke() : MutableList<DomainReceiveCardData>{
        return cardRepository.getCardListRepository()
    }
}