package com.example.domain.repo

import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.receive.DomainUpdateCardData
import com.example.domain.model.send.DomainSendCardData

/**
 * 2023-07-23
 * pureum
 */
interface CardRepository {
    suspend fun getCardListRepository():MutableList<DomainReceiveCardData>
    suspend fun insertCardUseCase(domainSendCardData: DomainSendCardData): String
    suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData):DomainServerReponse
}