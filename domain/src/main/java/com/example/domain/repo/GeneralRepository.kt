package com.example.domain.repo

import com.example.domain.model.receive.*
import com.example.domain.model.receive.bill.ServerBillData
import com.example.domain.model.receive.bill.ServerPictureData
import com.example.domain.model.receive.bill.ServerStoreData
import com.example.domain.model.receive.card.DomainUpadateData
import com.example.domain.model.send.DomainSendData

/**
 * 2023-07-23
 * pureum
 */
interface GeneralRepository {
    suspend fun deleteDataRepository(id: Long): ServerUidData
    suspend fun getDataListRepository(): ServerBillData
    suspend fun getStoreListRepository(): ServerStoreData
    suspend fun getPictureDataUseCaseRepository(uid:String): ServerPictureData
    suspend fun insertDataRepository(domainSendData: DomainSendData): ServerUidData
    suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData): ServerUidData
}