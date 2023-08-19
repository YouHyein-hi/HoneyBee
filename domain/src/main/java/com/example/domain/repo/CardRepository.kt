package com.example.domain.repo

import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.model.receive.DomainUpdateCardData
import com.example.domain.model.receive.CardResponseData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.send.DomainSendCardData

/**
 * 2023-07-23
 * pureum
 */
interface CardRepository {
    suspend fun getCardListRepository(): CardResponseData
    suspend fun insertCardUseCase(domainSendCardData: DomainSendCardData): ServerResponseData
//    suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData):DomainServerResponse
}