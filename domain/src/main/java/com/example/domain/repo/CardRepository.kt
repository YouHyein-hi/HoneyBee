package com.example.domain.repo

import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.card.SendCardData

/**
 * 2023-07-23
 * pureum
 */
interface CardRepository {
    suspend fun getCardListRepository(): ServerCardData
    suspend fun insertCardRepository(sendCardData: SendCardData): ServerResponseData
//    suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData):DomainServerResponse
    suspend fun getCardSpinnerRepository(): ServerCardSpinnerData
}