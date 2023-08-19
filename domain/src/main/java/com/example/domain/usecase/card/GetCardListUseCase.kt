package com.example.domain.usecase.card

import android.util.Log
import com.example.domain.model.receive.CardResponseData
import com.example.domain.repo.CardRepository

/**
 * 2023-07-23
 * pureum
 */
class GetCardListUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke() : CardResponseData{
        return cardRepository.getCardListRepository()
    }
}