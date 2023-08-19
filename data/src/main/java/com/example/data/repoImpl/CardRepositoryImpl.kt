package com.example.data.repoImpl

import android.util.Log
import com.example.data.remote.dataSource.CardDataSource
import com.example.data.remote.model.toDomainServerResponse
import com.example.data.remote.model.toServerCardData
import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.model.receive.DomainUpdateCardData
import com.example.domain.model.receive.CardResponseData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.repo.CardRepository
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class CardRepositoryImpl @Inject constructor(
    private val cardDataSource: CardDataSource
): CardRepository {
    override suspend fun getCardListRepository(): CardResponseData {
        return cardDataSource.getCardDataSource().toServerCardData()
    }

    override suspend fun insertCardUseCase(domainSendCardData: DomainSendCardData): DomainServerResponse {
        return cardDataSource.sendCardDataSource(
            cardName = domainSendCardData.cardName,
            amount = domainSendCardData.cardAmount,
            billCheckDate = domainSendCardData.billCheckDate
        )
    }


    override suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData): DomainServerResponse {
        Log.e("TAG", "updateCardDataRepo: ", )
        return cardDataSource.updateCardDataSource(
            id = domainUpdateCardData.id,
            cardName = domainUpdateCardData.cardName,
            cardAmount = domainUpdateCardData.cardAmount
        ).toDomainServerResponse()
    }
}