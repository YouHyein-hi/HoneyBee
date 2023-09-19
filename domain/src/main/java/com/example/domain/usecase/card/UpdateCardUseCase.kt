package com.example.domain.usecase.card

import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.send.card.SendUpdateCardData
import com.example.domain.repo.CardRepository


class UpdateCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(sendUpdateCardData: SendUpdateCardData): ServerUidData {
        return cardRepository.updateCardRepository(sendUpdateCardData)
    }
}