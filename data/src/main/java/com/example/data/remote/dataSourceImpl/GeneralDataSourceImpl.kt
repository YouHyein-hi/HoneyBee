package com.example.data.remote.dataSourceImpl

import ServerResponse
import com.example.data.remote.dataSource.GeneralDataSource
import com.example.data.remote.model.ServerBillResponse
import okhttp3.MultipartBody
import retrofit2.Retrofit
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class GeneralDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
): GeneralDataSource {
    override suspend fun sendDataSource(
        cardName: MultipartBody.Part,
        amount: MultipartBody.Part,
        pictureName: MultipartBody.Part,
        timestmap: MultipartBody.Part,
        bill: MultipartBody.Part
    ): ServerResponse<Int> {
        return retrofit.create(GeneralDataSource::class.java).sendDataSource(
            cardName = cardName,
            amount = amount,
            storeName = pictureName,
            billSubmitTime = timestmap,
            file = bill
        )
    }

    override suspend fun receiveDataSource(): ServerResponse<List<ServerBillResponse>> {
        return retrofit.create(GeneralDataSource::class.java).receiveDataSource()
    }

    override suspend fun receivePictureDataSource(uid:String): ServerResponse<String> {
        return retrofit.create(GeneralDataSource::class.java).receivePictureDataSource(uid)
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
}