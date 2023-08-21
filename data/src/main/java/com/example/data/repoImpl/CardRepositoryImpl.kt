package com.example.data.repoImpl

import com.example.data.remote.dataSource.CardDataSource
import com.example.domain.model.receive.ServerCardData
import com.example.domain.model.receive.ServerCardSpinnerData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.repo.CardRepository
import com.example.domain.util.changeAmount
import com.example.domain.util.changeDate
import toServerCardData
import toServerCardSpinnerData
import toServerResponseData
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class CardRepositoryImpl @Inject constructor(
    private val cardDataSource: CardDataSource
): CardRepository {
    override suspend fun getCardListRepository(): ServerCardData {
        val response = cardDataSource.getCardDataSource().toServerCardData()
        val newList = response.body?.map { it.copy(amount = changeAmount(it.amount)) }
        return ServerCardData(response.status, response.message, newList)
    }

    override suspend fun getCardSpinnerRepository(): ServerCardSpinnerData {
        val response = cardDataSource.getCardDataSource().toServerCardSpinnerData()
        val newList = response.body?.map { it.copy(amount = changeAmount(it.amount)) }
        return ServerCardSpinnerData(response.status, response.message, newList)
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