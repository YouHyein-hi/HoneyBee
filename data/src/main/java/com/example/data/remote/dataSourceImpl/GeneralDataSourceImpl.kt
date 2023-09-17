package com.example.data.remote.dataSourceImpl

import ServerResponse
import com.example.data.di.RetrofitModule
import com.example.data.remote.dataSource.GeneralDataSource
import com.example.data.remote.model.ServerBillResponse
import com.example.data.remote.model.ServerDetailBillResponse
import okhttp3.MultipartBody
import retrofit2.Retrofit
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class GeneralDataSourceImpl @Inject constructor(
    @RetrofitModule.Api private val retrofit: Retrofit
): GeneralDataSource {

    override suspend fun sendDataSource(
        cardName: MultipartBody.Part,
        storeName: MultipartBody.Part,
        billSubmitTime: MultipartBody.Part,
        amount: MultipartBody.Part,
        file: MultipartBody.Part,
        billMemo: MultipartBody.Part
    ): ServerResponse<Int> {
        return retrofit.create(GeneralDataSource::class.java).sendDataSource(
            cardName = cardName,
            amount = amount,
            storeName = storeName,
            billSubmitTime = billSubmitTime,
            file = file,
            billMemo = billMemo
        )
    }

    override suspend fun getBillListDataSource(): ServerResponse<List<ServerBillResponse>> {
        return retrofit.create(GeneralDataSource::class.java).getBillListDataSource()
    }

    override suspend fun getDetailBillData(id: String): ServerResponse<ServerDetailBillResponse> {
        return retrofit.create(GeneralDataSource::class.java).getDetailBillData(id)
    }

    override suspend fun getStoreListDataSource(): ServerResponse<List<String>> {
        return retrofit.create(GeneralDataSource::class.java).getStoreListDataSource()
    }

    override suspend fun getPictureDataSource(uid:String): ServerResponse<String> {
        return retrofit.create(GeneralDataSource::class.java).getPictureDataSource(uid)
    }

    override suspend fun deleteServerData(uid:Long): ServerResponse<Int> {
        return retrofit.create(GeneralDataSource::class.java).deleteServerData(uid)
    }

    override suspend fun updateDataSource(
        id: Long,
        cardName: String,
        storeName: String,
        billSubmitTime: LocalDateTime,
        amount: Int
    ): ServerResponse<Int> {
        return retrofit.create(GeneralDataSource::class.java).updateDataSource(
            id,
            cardName,
            storeName,
            billSubmitTime,
            amount
        )
    }

    override suspend fun billCheckCompleteDataSource(): ServerResponse<String?> {
        return retrofit.create(GeneralDataSource::class.java).billCheckCompleteDataSource()
    }

    override suspend fun billCheckCancelDataSource(): ServerResponse<String?> {
        return retrofit.create(GeneralDataSource::class.java).billCheckCancelDataSource()
    }
}