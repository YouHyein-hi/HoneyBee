package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.ReceiveSource
import com.example.data.remote.dto.ReceiveData
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class ReceiveSourceImpl @Inject constructor(
    private val retrofit: Retrofit
):ReceiveSource{
    override suspend fun receiveDataSource(): ReceiveData {
        return retrofit.create(ReceiveSource::class.java).receiveDataSource()
    }
}