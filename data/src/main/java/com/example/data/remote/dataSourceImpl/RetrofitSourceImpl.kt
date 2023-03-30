package com.example.data.remote.dataSourceImpl

import android.util.Log
import com.example.data.remote.dataSource.RetrofitSource
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
        val gap = retrofit.create(RetrofitSource::class.java).sendDataSource(
        cardName = cardName,
        amount = amount,
        pictureName = pictureName,
        timestmap = timestmap,
        bill = bill
        )
        Log.e("TAG", "sendDataSource: $gap", )
        return gap
    }

    override suspend fun receiveDataSource(): MutableList<ReceiveData> {
        return retrofit.create(RetrofitSource::class.java).receiveDataSource()
    }

    override suspend fun deleteServerData(): String {
        return retrofit.create(RetrofitSource::class.java).deleteServerData()
    }

    override suspend fun sendCardSource(
        cardList : MultipartBody.Part
    ): String{
        Log.e("TAG", "sendCardSource: 들어감", )
        val cl = retrofit.create(RetrofitSource::class.java).sendCardSource(
            cardList = cardList
        )  // 여기서 오류가 난건뎅
        Log.e("TAG", "sendCardSource: ${cl}", )
        return cl
    }


}