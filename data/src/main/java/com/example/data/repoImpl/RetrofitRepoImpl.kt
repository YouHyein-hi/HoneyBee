package com.example.data.repoImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.toDomainReceiveData
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody
import javax.inject.Inject


/**
 * 2023-02-02
 * pureum
 */
class RetrofitRepoImpl @Inject constructor(
    private val retrofitSource: RetrofitSource,
):RetrofitRepo{
    override suspend fun sendDataRepo(card:MultipartBody.Part, amount:MultipartBody.Part, pictureName:MultipartBody.Part, date:MultipartBody.Part, picture:MultipartBody.Part): String {
        return retrofitSource.sendDataSource(cardName = card, amount = amount, pictureName=pictureName, timestmap = date, bill = picture)
    }

    override suspend fun receiveDataRepo(): DomainReceiveData {
        return retrofitSource.receiveDataSource().toDomainReceiveData()
    }

    override suspend fun test(): String {
        return retrofitSource.test("ㅇ")
    }


}