package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.GeneralDataSurce
import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.model.ReceiveData
import com.example.data.remote.model.ServerResponse
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
): GeneralDataSurce {
    override suspend fun sendDataSource(
        cardName: MultipartBody.Part,
        amount: MultipartBody.Part,
        pictureName: MultipartBody.Part,
        timestmap: MultipartBody.Part,
        bill: MultipartBody.Part
    ): String {
        return retrofit.create(RetrofitSource::class.java).sendDataSource(
            cardName = cardName,
            amount = amount,
            storeName = pictureName,
            billSubmitTime = timestmap,
            file = bill
        )
    }

    override suspend fun receiveDataSource(): MutableList<ReceiveData> {
        return retrofit.create(RetrofitSource::class.java).receiveDataSource()
    }

    override suspend fun receivePictureDataSource(uid:String): String {
        return retrofit.create(RetrofitSource::class.java).receivePictureDataSource(uid)
    }


    override suspend fun deleteServerData(uid:Long): String {
        return retrofit.create(RetrofitSource::class.java).deleteServerData(uid)
    }


    override suspend fun updateDataSource(
        id: Long,
        cardName: String,
        storeName: String,
        billSubmitTime: LocalDateTime,
        amount: String
    ): ServerResponse {
        TODO("Not yet implemented")
    }
}