package com.example.data.repoImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.toDomainReceiveData
import com.example.data.remote.dto.toDomainSendData
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class RetrofitRepoImpl @Inject constructor(
    private val retrofitSource: RetrofitSource,
):RetrofitRepo{

    override suspend fun sendDataRepo(date:String, amount : Int, card:String, picture:ByteArray): DomainSendData {
        return retrofitSource.sendDataSource(date,amount, card, picture).toDomainSendData()
    }

    override suspend fun receiveDataRepo(): DomainReceiveData {
        return retrofitSource.receiveDataSource().toDomainReceiveData()
    }
}