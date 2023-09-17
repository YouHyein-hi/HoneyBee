package com.example.domain.usecase.card

import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.repo.CardRepository

class DeleteCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(id:Long) : ServerUidData {
        return cardRepository.deleteCardRepository(id)
    }
}