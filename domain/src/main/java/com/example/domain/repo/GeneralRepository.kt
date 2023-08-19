package com.example.domain.repo

import android.graphics.Bitmap
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.send.DomainSendData

/**
 * 2023-07-23
 * pureum
 */
interface GeneralRepository {
    suspend fun deleteDataRepository(id: Long):String
    suspend fun getDataListRepository():MutableList<DomainReceiveAllData>
    suspend fun getPictureDataUseCaseRepository(uid:String): Bitmap
    suspend fun insertDataRepository(domainSendData: DomainSendData): String
    suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData)
//    suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData): DomainServerResponse
}