package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.GeneralDataSource
import com.example.data.remote.model.ReceiveData
import com.example.data.remote.model.ServerResponse
import com.example.domain.model.receive.SimpleResponse
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
    ): String {
        return retrofit.create(GeneralDataSource::class.java).sendDataSource(
            cardName = cardName,
            amount = amount,
            storeName = pictureName,
            billSubmitTime = timestmap,
            file = bill
        )
    }

    override suspend fun receiveDataSource(): MutableList<ReceiveData> {
        return retrofit.create(GeneralDataSource::class.java).receiveDataSource()
    }

    override suspend fun receivePictureDataSource(uid:String): String {
        return retrofit.create(GeneralDataSource::class.java).receivePictureDataSource(uid)
    }

    override suspend fun deleteServerData(uid:Long): String {
        return retrofit.create(GeneralDataSource::class.java).deleteServerData(uid)
    }


    override suspend fun updateDataSource(
        id: Long,
        cardName: String,
        storeName: String,
        billSubmitTime: LocalDateTime,
        amount: Int
    ): ServerResponse<SimpleResponse> {
        return retrofit.create(GeneralDataSource::class.java).updateDataSource(
            id,
            cardName,
            storeName,
            billSubmitTime,
            amount
        )
    }
}