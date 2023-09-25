package com.example.domain.repo

import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.receive.card.ServerCardDetailData
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.model.remote.send.card.SendUpdateCardData


interface CardRepository {
    suspend fun getCardListRepository(): ServerCardData
    suspend fun insertCardRepository(sendCardData: SendCardData): ServerResponseData
    suspend fun getCardSpinnerRepository(): ServerCardSpinnerData
    suspend fun deleteCardRepository(id: Long): ServerUidData
    suspend fun updateCardRepository(sendUpdateCardData: SendUpdateCardData): ServerUidData
    suspend fun getCardDetailRepository(id: String): ServerCardDetailData
}