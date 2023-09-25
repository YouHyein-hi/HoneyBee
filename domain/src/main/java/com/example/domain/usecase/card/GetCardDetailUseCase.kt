package com.example.domain.usecase.card

import com.example.domain.model.remote.receive.card.ServerCardDetailData
import com.example.domain.repo.CardRepository

class GetCardDetailUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(id: String) : ServerCardDetailData {
        return cardRepository.getCardDetailRespository(id)
    }
}