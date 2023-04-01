package com.example.data.repoImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.toDomainReceiveCardData
import com.example.domain.model.DomainReceiveAllData
import com.example.domain.model.DomainReceiveCardData
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

    override suspend fun receiveDataRepo(): MutableList<DomainReceiveAllData> {
        return retrofitSource.receiveDataSource().map { DomainReceiveAllData(it.cardName, it.amount, it.date, it.pictureName, it.picture) }.toMutableList()
    }

    override suspend fun deleteServerData(date:String): String {
        return retrofitSource.deleteServerData()
    }


    override suspend fun sendCardDataRepo(card: MultipartBody.Part, amount: MultipartBody.Part): String {
        return retrofitSource.sendCardDataSource(cardName = card,amount = amount)
    }

    override suspend fun receiveCardDataRepo(): MutableList<DomainReceiveCardData> {
        return retrofitSource.receiveCardDataSource().map { it.toDomainReceiveCardData() }.toMutableList()
    }

    override suspend fun deleteCardDataRepo(): String {
        return retrofitSource.deleteCardDataSource()
    }

}