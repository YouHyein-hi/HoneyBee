package com.example.data.repoImpl

import com.example.data.remote.dataSource.CardDataSource
import com.example.data.remote.model.toCardResponseData
import com.example.data.remote.model.toServerResponseData
import com.example.domain.model.receive.CardResponseData
import com.example.domain.model.receive.ServerResponseData
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
        return cardDataSource.getCardDataSource().toCardResponseData()
    }

    override suspend fun insertCardUseCase(domainSendCardData: DomainSendCardData): ServerResponseData {
        return cardDataSource.sendCardDataSource(
            cardName = domainSendCardData.cardName,
            amount = domainSendCardData.cardAmount,
            billCheckDate = domainSendCardData.billCheckDate
        ).toServerResponseData()
    }


//    override suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData): DomainServerResponse {
//        Log.e("TAG", "updateCardDataRepo: ", )
//        return cardDataSource.updateCardDataSource(
//            id = domainUpdateCardData.id,
//            cardName = domainUpdateCardData.cardName,
//            cardAmount = domainUpdateCardData.cardAmount
//        ).toDomainServerResponse()
//    }
}