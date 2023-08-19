package com.example.domain.repo

import android.graphics.Bitmap
import com.example.domain.model.receive.*
import com.example.domain.model.send.DomainSendData

/**
 * 2023-07-23
 * pureum
 */
interface GeneralRepository {
    suspend fun deleteDataRepository(id: Long): ServerResponseData
    suspend fun getDataListRepository(): BillResponseData
    suspend fun getPictureDataUseCaseRepository(uid:String): ServerPictureData
    suspend fun insertDataRepository(domainSendData: DomainSendData): ServerResponseData
    suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData): ServerResponseData
}