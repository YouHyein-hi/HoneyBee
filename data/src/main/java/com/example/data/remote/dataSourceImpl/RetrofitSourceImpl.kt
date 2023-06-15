package com.example.data.remote.dataSourceImpl

import android.util.Log
import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.model.ReceiveCardData
import com.example.data.remote.model.ReceiveData
import okhttp3.MultipartBody
import okio.utf8Size
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
            storeName = pictureName,
            date = timestmap,
            file = bill
        )
    }
    override suspend fun sendCardDataSource(
        cardName: String?,
        amount: Int?
    ): String {
        return retrofit.create(RetrofitSource::class.java).sendCardDataSource(cardName = cardName, amount = amount)
    }

    override suspend fun receiveDataSource(): MutableList<ReceiveData> {
        return retrofit.create(RetrofitSource::class.java).receiveDataSource()
    }

    override suspend fun receiveCardDataSource(): MutableList<ReceiveCardData> {
        return retrofit.create(RetrofitSource::class.java).receiveCardDataSource()
    }

    override suspend fun receivePictureDataSource(uid:String): String {
        return retrofit.create(RetrofitSource::class.java).receivePictureDataSource(uid)
    }

    override suspend fun deleteServerData(uid:Long): String {
        return retrofit.create(RetrofitSource::class.java).deleteServerData(uid)
    }

    override suspend fun deleteCardDataSource(uid:Long): String {
        return retrofit.create(RetrofitSource::class.java).deleteCardDataSource(uid)
    }

    override suspend fun resendDataSource(
        id: MultipartBody.Part,
        cardName: MultipartBody.Part,
        amount: MultipartBody.Part,
        storeName: MultipartBody.Part,
        date: MultipartBody.Part,
    ): String {
        return retrofit.create(RetrofitSource::class.java).resendDataSource(
            id = id,
            cardName = cardName,
            amount = amount,
            storeName = storeName,
            date = date,
        )
    }

    override suspend fun resendCardDataSource(
        cardName: String, cardAmount: Int
    ): String {
        return retrofit.create(RetrofitSource::class.java).resendCardDataSource(
            cardName = cardName,
            cardAmount = cardAmount
        )
    }


    override suspend fun myTest(file: MultipartBody.Part): String {
        Log.e("TAG", "myTest: test $file")
        return retrofit.create(RetrofitSource::class.java).myTest(file)
    }

}