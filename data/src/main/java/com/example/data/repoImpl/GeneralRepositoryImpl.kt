package com.example.data.repoImpl

import android.graphics.BitmapFactory
import android.util.Base64
import com.example.data.mapper.ResponseMapper.toServerBillData
import com.example.data.mapper.ResponseMapper.toServerStoreData
import com.example.data.mapper.ResponseMapper.toUidServerResponseData
import com.example.data.remote.dataSource.GeneralDataSource
import com.example.domain.model.receive.*
import com.example.domain.model.receive.bill.ServerBillData
import com.example.domain.model.receive.bill.ServerPictureData
import com.example.domain.model.receive.bill.ServerStoreData
import com.example.domain.model.receive.card.DomainUpadateData
import com.example.domain.model.send.DomainSendData
import com.example.domain.repo.GeneralRepository
import com.example.domain.util.StringUtil
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class GeneralRepositoryImpl @Inject constructor(
    private val generalDataSource: GeneralDataSource
): GeneralRepository {
    override suspend fun deleteDataRepository(id: Long): ServerUidData {
        return generalDataSource.deleteServerData(id).toUidServerResponseData()
    }

    override suspend fun getDataListRepository(): ServerBillData {
        val result = generalDataSource.getBillListDataSource().toServerBillData()
        val newList = result.body?.map { it.copy(date = StringUtil.changeDate(it.date), storeAmount = StringUtil.changeAmount(it.storeAmount)) }
        return ServerBillData(result.status, result.message, newList)
    }

    override suspend fun getStoreListRepository(): ServerStoreData {
        return generalDataSource.getStoreListDataSource().toServerStoreData()
    }

    override suspend fun getPictureDataUseCaseRepository(uid: String): ServerPictureData {
        val response = generalDataSource.getPictureDataSource(uid)
        val byteData = response.body
        val decode = Base64.decode(byteData, Base64.DEFAULT)
        return ServerPictureData(status = response.status, message = response.message, picture = BitmapFactory.decodeByteArray(decode, 0, decode.size))
    }

    override suspend fun insertDataRepository(domainSendData: DomainSendData): ServerUidData {
        return generalDataSource.sendDataSource(
            cardName = domainSendData.cardName,
            amount = domainSendData.amount,
            storeName = domainSendData.storeName,
            billSubmitTime = domainSendData.date,
            file = domainSendData.picture
        ).toUidServerResponseData()
    }

    override suspend fun updateDataUseCaseRepository(domainResendData: DomainUpadateData): ServerUidData {
        return generalDataSource.updateDataSource(
            id = domainResendData.id,
            cardName = domainResendData.cardName,
            amount = domainResendData.amount,
            storeName = domainResendData.storeName,
            billSubmitTime = domainResendData.billSubmitTime,
        ).toUidServerResponseData()
    }
}