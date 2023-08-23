package com.example.domain.usecase.card

import com.example.domain.model.receive.card.ServerCardSpinnerData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class GetCardSpinnerUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke() : ServerCardSpinnerData {
        return cardRepository.getCardSpinnerRepository()
    }
}