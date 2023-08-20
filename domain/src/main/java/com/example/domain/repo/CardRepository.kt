package com.example.domain.repo

import com.example.domain.model.receive.ServerCardData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.receive.ServerUidData
import com.example.domain.model.send.DomainSendCardData

/**
 * 2023-07-23
 * pureum
 */
interface CardRepository {
    suspend fun getCardListRepository(): ServerCardData
    suspend fun insertCardUseCase(domainSendCardData: DomainSendCardData): ServerResponseData
//    suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData):DomainServerResponse
}