package com.example.data.repoImpl

import android.graphics.BitmapFactory
import android.util.Base64
import com.example.data.remote.dataSource.GeneralDataSource
import com.example.data.remote.model.toServerBillData
import com.example.data.remote.model.toServerResponseData
import com.example.domain.model.receive.*
import com.example.domain.model.send.DomainSendData
import com.example.domain.repo.GeneralRepository
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class GeneralRepositoryImpl @Inject constructor(
    private val generalDataSource: GeneralDataSource
): GeneralRepository {
    override suspend fun deleteDataRepository(id: Long): ServerResponseData {
        return generalDataSource.deleteServerData(id).toServerResponseData()
    }

    override suspend fun getDataListRepository(): BillResponseData {
        return generalDataSource.receiveDataSource().toServerBillData()
    }

    override suspend fun getPictureDataUseCaseRepository(uid: String): ServerPictureData {
        val response = generalDataSource.receivePictureDataSource(uid)
        val byteData = generalDataSource.receivePictureDataSource(uid).body?.first().toString()
        val decode = Base64.decode(byteData, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decode, 0, decode.size)
        return ServerPictureData(status = response.status, message = response.message, picture = BitmapFactory.decodeByteArray(decode, 0, decode.size))
    }

    override suspend fun insertDataRepository(domainSendData: DomainSendData): ServerResponseData {
        return generalDataSource.sendDataSource(
            cardName = domainSendData.cardName,
            amount = domainSendData.amount,
            storeName = domainSendData.storeName,
            billSubmitTime = domainSendData.date,
            file = domainSendData.picture
        ).toServerResponseData()
    }

    override suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData): ServerResponseData {
        return generalDataSource.updateDataSource(
            id = domainResendData.id,
            cardName = domainResendData.cardName,
            amount = domainResendData.amount,
            storeName = domainResendData.storeName,
            billSubmitTime = domainResendData.billSubmitTime,
        ).toServerResponseData()
    }
}