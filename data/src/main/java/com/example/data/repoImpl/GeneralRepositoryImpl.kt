package com.example.data.repoImpl

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.data.mapper.ResponseMapper.toServerBillData
import com.example.data.mapper.ResponseMapper.toServerDetailBillData
import com.example.data.mapper.ResponseMapper.toServerResponseData
import com.example.data.mapper.ResponseMapper.toServerStoreData
import com.example.data.mapper.ResponseMapper.toUidServerResponseData
import com.example.data.remote.dataSource.GeneralDataSource
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.receive.bill.ServerBillData
import com.example.domain.model.remote.receive.bill.ServerPictureData
import com.example.domain.model.remote.receive.bill.ServerStoreData
import com.example.domain.model.remote.send.bill.SendBillUpdateData
import com.example.domain.model.ui.bill.UiBillData
import com.example.domain.repo.GeneralRepository
import com.example.domain.util.StringUtil
import com.example.data.util.UriToBitmapUtil
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.receive.bill.ServerDetailBillData
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class GeneralRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val generalDataSource: GeneralDataSource
): GeneralRepository {
    override suspend fun deleteDataRepository(id: Long): ServerUidData {
        return generalDataSource.deleteServerData(id).toUidServerResponseData()
    }

    override suspend fun getDataListRepository(): ServerBillData {
        val result = generalDataSource.getBillListDataSource().toServerBillData()
        val newList = result.body?.map { it.copy(billSubmitTime = StringUtil.changeDate(it.billSubmitTime), storeAmount = StringUtil.changeAmount(it.storeAmount)) }
        return ServerBillData(result.status, result.message, newList)
    }

    override suspend fun getDetailDataRepository(id: String): ServerDetailBillData {
        return generalDataSource.getDetailBillData(id).toServerDetailBillData()
    }

    override suspend fun getStoreListRepository(): ServerStoreData {
        return generalDataSource.getStoreListDataSource().toServerStoreData()
    }

    override suspend fun getPictureDataRepository(id: String): ServerPictureData {
        val response = generalDataSource.getPictureDataSource(id)
        val byteData = response.body
        val decode = Base64.decode(byteData, Base64.DEFAULT)
        return ServerPictureData(status = response.status, message = response.message, picture = BitmapFactory.decodeByteArray(decode, 0, decode.size))
    }

    override suspend fun insertDataRepository(data: UiBillData): ServerUidData {
        return generalDataSource.sendDataSource(
            cardName = MultipartBody.Part.createFormData(
                "cardName",
                data.cardName
            ),
            amount = MultipartBody.Part.createFormData(
                "amount",
                data.storeAmount.replace(",", "")
            ),
            storeName = MultipartBody.Part.createFormData(
                "storeName",
                data.storeName
            ),
            billSubmitTime = MultipartBody.Part.createFormData(
                "billSubmitTime",
                data.date
            ),
            file = UriToBitmapUtil(context, data.picture),
            billMemo = MultipartBody.Part.createFormData(
                "billMemo",
                data.memo
            )
        ).toUidServerResponseData()
    }

    override suspend fun updateDataRepository(sendBillUpdateData: SendBillUpdateData): ServerUidData {
        return generalDataSource.updateDataSource(
            id = sendBillUpdateData.id,
            cardName = sendBillUpdateData.cardName,
            storeName = sendBillUpdateData.storeName,
            billSubmitTime = sendBillUpdateData.date,
            amount = sendBillUpdateData.storeAmount,
        ).toUidServerResponseData()
    }

    override suspend fun billCheckCompleteRepository(): ServerResponseData {
        return generalDataSource.billCheckCompleteDataSource().toServerResponseData()
    }

    override suspend fun billCheckCancelRepository(): ServerResponseData {
        return generalDataSource.billCheckCancelDataSource().toServerResponseData()
    }
}