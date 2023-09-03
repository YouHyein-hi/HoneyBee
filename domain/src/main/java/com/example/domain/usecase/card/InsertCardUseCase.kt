package com.example.domain.usecase.card

import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class InsertCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(sendCardData: SendCardData) : ServerResponseData {
        return cardRepository.insertCardRepository(sendCardData)
    }
}