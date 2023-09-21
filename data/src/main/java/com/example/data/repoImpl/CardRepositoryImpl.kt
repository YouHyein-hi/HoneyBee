package com.example.data.repoImpl

import android.util.Log
import com.example.data.manager.PreferenceManager
import com.example.data.mapper.ResponseMapper.toServerCardData
import com.example.data.mapper.ResponseMapper.toServerCardSpinnerData
import com.example.data.mapper.ResponseMapper.toServerResponseData
import com.example.data.mapper.ResponseMapper.toUidServerResponseData
import com.example.data.remote.dataSource.CardDataSource
import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.model.remote.send.card.SendUpdateCardData
import com.example.domain.repo.CardRepository
import com.example.domain.util.StringUtil
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class CardRepositoryImpl @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val preferenceManager: PreferenceManager
): CardRepository {
    override suspend fun getCardListRepository(): ServerCardData {
        val response = cardDataSource.getCardDataSource().toServerCardData()
        var newList = response.body?.map { it.copy(cardAmount = StringUtil.changeAmount(it.cardAmount)) }
        if(preferenceManager.getUserRight()=="BS") newList = newList?.map { it.copy(cardAmount = "-") }
        return ServerCardData(response.status, response.message, newList)
    }

    override suspend fun getCardSpinnerRepository(): ServerCardSpinnerData {
        val response = cardDataSource.getCardDataSource().toServerCardSpinnerData()
        var newList = response.body?.map { it.copy(amount = StringUtil.changeAmount(it.amount)) }
        if(preferenceManager.getUserRight()=="BS") newList = newList?.map { it.copy(amount = "-") }
        return ServerCardSpinnerData(response.status, response.message, newList)
    }

    override suspend fun deleteCardRepository(id: Long): ServerUidData {
        return cardDataSource.deleteCardDataSource(id).toUidServerResponseData()
    }

    override suspend fun insertCardRepository(sendCardData: SendCardData): ServerResponseData {
        return cardDataSource.sendCardDataSource(
            cardName = sendCardData.cardName,
            amount = sendCardData.cardAmount,
            expireDate = sendCardData.cardExpireDate,
            designId = sendCardData.cardDesignId
        ).body()!!.toServerResponseData()
    }

    override suspend fun updateCardRepository(sendUpdateCardData: SendUpdateCardData) : ServerUidData {
        return cardDataSource.updateCardDataSource(
            id = sendUpdateCardData.id,
            cardName = sendUpdateCardData.cardName,
            cardAmount = sendUpdateCardData.cardAmount,
            cardExpireDate = sendUpdateCardData.cardExpireDate,
            cardDesignId = sendUpdateCardData.cardDesignId
        ).toUidServerResponseData()
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