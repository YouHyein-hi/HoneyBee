package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.CardDataSource
import com.example.data.remote.model.ReceiveCardData
import com.example.data.remote.model.ServerResponse
import com.example.domain.model.receive.DomainServerReponse
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class CardDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
): CardDataSource{
    override suspend fun sendCardDataSource(
        cardName: String?,
        amount: Int?,
        billCheckDate : String?
    ): DomainServerReponse {
        return retrofit.create(CardDataSource::class.java).sendCardDataSource(cardName = cardName, amount = amount, billCheckDate = billCheckDate)
    }

    override suspend fun receiveCardDataSource(): MutableList<ReceiveCardData> {
        return retrofit.create(CardDataSource::class.java).receiveCardDataSource()
    }

    override suspend fun deleteCardDataSource(uid:Long): String {
        return retrofit.create(CardDataSource::class.java).deleteCardDataSource(uid)
    }

    override suspend fun updateCardDataSource(
        id : Long, cardName: String, cardAmount: Int
    ): ServerResponse {
        return retrofit.create(CardDataSource::class.java).updateCardDataSource(
            id = id,
            cardName = cardName,
            cardAmount = cardAmount,
        )
    }
}