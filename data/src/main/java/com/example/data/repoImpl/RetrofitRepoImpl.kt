package com.example.data.repoImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.toDomainReceiveData
import com.example.data.remote.dto.toDomainSendData
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class RetrofitRepoImpl @Inject constructor(
    private val retrofitSource: RetrofitSource,
):RetrofitRepo{

    override suspend fun sendDataRepo(date: LocalDateTime, amount : Int, card:String, picture: MultipartBody.Part): DomainSendData {
        return retrofitSource.sendDataSource(date,amount, card, picture).toDomainSendData()
    }

    override suspend fun receiveDataRepo(): DomainReceiveData {
        return retrofitSource.receiveDataSource().toDomainReceiveData()
    }
}