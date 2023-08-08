package com.example.data.repoImpl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.data.remote.dataSource.GeneralDataSource
import com.example.data.remote.model.toDomainLoginResponse
import com.example.data.remote.model.toDomainReceiveData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpadateData
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
    override suspend fun deleteDataRepository(id: Long): String {
        return generalDataSource.deleteServerData(id)
    }

    override suspend fun getDataListRepository(): MutableList<DomainReceiveAllData> {
        return generalDataSource.receiveDataSource().map { it.toDomainReceiveData() }.toMutableList()
    }

    override suspend fun getPictureDataUseCaseRepository(uid: String): Bitmap {
        val byteData = generalDataSource.receivePictureDataSource(uid)
        val decode = Base64.decode(byteData, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decode, 0, decode.size)    }

    override suspend fun insertDataRepository(domainSendData: DomainSendData): String {
        return generalDataSource.sendDataSource(
            cardName = domainSendData.cardName,
            amount = domainSendData.amount,
            storeName = domainSendData.storeName,
            billSubmitTime = domainSendData.date,
            file = domainSendData.picture
        )    }

    override suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData): DomainServerReponse {
        return generalDataSource.updateDataSource(
            id = domainResendData.id,
            cardName = domainResendData.cardName,
            amount = domainResendData.amount,
            storeName = domainResendData.storeName,
            billSubmitTime = domainResendData.billSubmitTime,
        ).toDomainLoginResponse()    }
}