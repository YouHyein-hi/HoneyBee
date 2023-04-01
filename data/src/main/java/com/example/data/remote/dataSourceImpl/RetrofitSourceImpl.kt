package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.ReceiveCardData
import com.example.data.remote.dto.ReceiveData
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class RetrofitSourceImpl @Inject constructor(
    private val retrofit: Retrofit,
) : RetrofitSource {


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
        pictureName = pictureName,
        timestmap = timestmap,
        bill = bill
        )
    }
    override suspend fun receiveDataSource(): MutableList<ReceiveData> {
        return retrofit.create(RetrofitSource::class.java).receiveDataSource()
    }

    override suspend fun deleteServerData(): String {
        return retrofit.create(RetrofitSource::class.java).deleteServerData()
    }

    override suspend fun sendCardDataSource(cardName: MultipartBody.Part, amount: MultipartBody.Part): String {
        return retrofit.create(RetrofitSource::class.java).sendCardDataSource(cardName = cardName, amount = amount)
    }

    override suspend fun receiveCardDataSource(): MutableList<ReceiveCardData> {
        return retrofit.create(RetrofitSource::class.java).receiveCardDataSource()
    }

    override suspend fun deleteCardDataSource(): String {
        return retrofit.create(RetrofitSource::class.java).deleteCardDataSource()
    }


}