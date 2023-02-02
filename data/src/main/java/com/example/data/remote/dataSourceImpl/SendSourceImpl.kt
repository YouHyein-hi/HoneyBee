package com.example.data.remote.dataSourceImpl

import com.example.data.remote.dataSource.SendSource
import com.example.data.remote.dto.SendData
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class SendSourceImpl @Inject constructor(
    private val retrofit: Retrofit
):SendSource {
    override suspend fun sendDataSource(): SendData {
        return retrofit.create(SendSource::class.java).sendDataSource()
    }
}