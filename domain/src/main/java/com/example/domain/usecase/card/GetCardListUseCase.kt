package com.example.domain.usecase.card

import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class GetCardListUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke() : ServerCardData {
        return cardRepository.getCardListRepository()
    }
}