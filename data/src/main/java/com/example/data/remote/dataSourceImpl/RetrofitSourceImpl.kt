package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.ReceiveData
import com.example.data.remote.dto.SendData
import okhttp3.MultipartBody
import retrofit2.Retrofit
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class RetrofitSourceImpl @Inject constructor(
    private val retrofit: Retrofit,
):RetrofitSource{

    override suspend fun sendDataSource(date: LocalDateTime, amount : Int, card:String, picture: MultipartBody.Part): SendData {
        return retrofit.create(RetrofitSource::class.java).sendDataSource(date,amount, card, picture)
    }

    override suspend fun receiveDataSource(): ReceiveData {
        return retrofit.create(RetrofitSource::class.java).receiveDataSource()
    }
}