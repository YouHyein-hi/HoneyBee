package com.example.data.repoImpl

import android.util.Log
import com.example.data.remote.dataSource.CardDataSource
import com.example.data.remote.model.ServerResponse
import com.example.data.remote.model.toDomainLoginResponse
import com.example.data.remote.model.toDomainReceiveCardData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.receive.DomainUpdateCardData
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
    override suspend fun getCardListRepository(): MutableList<DomainReceiveCardData> {
        return cardDataSource.receiveCardDataSource().map { it.toDomainReceiveCardData() }
            .toMutableList()
    }

    override suspend fun insertCardUseCase(domainSendCardData: DomainSendCardData): DomainServerReponse {
        return cardDataSource.sendCardDataSource(
            cardName = domainSendCardData.cardName,
            amount = domainSendCardData.cardAmount,
            billCheckDate = domainSendCardData.billCheckDate
        )
    }


    override suspend fun updateCardUseCase(domainUpdateCardData: DomainUpdateCardData): DomainServerReponse {
        Log.e("TAG", "updateCardDataRepo: ", )
        return cardDataSource.updateCardDataSource(
            id = domainUpdateCardData.id,
            cardName = domainUpdateCardData.cardName,
            cardAmount = domainUpdateCardData.cardAmount
        ).toDomainLoginResponse()
    }




}