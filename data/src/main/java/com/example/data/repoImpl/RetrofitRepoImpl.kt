package com.example.data.repoImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.toDomainReceiveCardData
import com.example.domain.model.*
import com.example.domain.repo.RetrofitRepo
import okhttp3.MultipartBody
import javax.inject.Inject


/**
 * 2023-02-02
 * pureum
 */
class RetrofitRepoImpl @Inject constructor(
    private val retrofitSource: RetrofitSource,
) : RetrofitRepo {
    override suspend fun sendDataRepo(domainSendData: DomainSendData): String {
        return retrofitSource.sendDataSource(
            cardName = domainSendData.cardName,
            amount = domainSendData.amount,
            storeName = domainSendData.storeName,
            date = domainSendData.date,
            file = domainSendData.picture
        )
    }

    override suspend fun receiveDataRepo(): MutableList<DomainReceiveAllData> {
        return retrofitSource.receiveDataSource().map {
            DomainReceiveAllData(
                it.cardName,
                it.amount,
                it.date,
                it.pictureName,
                it.picture
            )
        }.toMutableList()
    }

    override suspend fun deleteServerData(id: Long): String {
        return retrofitSource.deleteServerData()
    }


    override suspend fun sendCardDataRepo(domainSendCardData: DomainSendCardData): String {
        return retrofitSource.sendCardDataSource(
            cardName = domainSendCardData.cardName,
            amount = domainSendCardData.cardAmount
        )
    }

    override suspend fun receiveCardDataRepo(): MutableList<DomainReceiveCardData> {
        return retrofitSource.receiveCardDataSource().map { it.toDomainReceiveCardData() }
            .toMutableList()
    }

    override suspend fun deleteCardDataRepo(id: Long): String {
        return retrofitSource.deleteCardDataSource()
    }

    override suspend fun resendDataRepo(domainResendData: DomainResendData): String {
        return retrofitSource.resendDataSource(
            id = domainResendData.id,
            cardName = domainResendData.cardName,
            amount = domainResendData.amount,
            storeName = domainResendData.storeName,
            date = domainResendData.date,
            file = domainResendData.picture
        )
    }

    override suspend fun resendCardDataRepo(domainResendCardData: DomainResendCardData): String {
        return retrofitSource.resendCardDataSource(
            cardName = domainResendCardData.cardName,
            cardAmount = domainResendCardData.cardAmount
        )
    }

    override suspend fun myTest(file: MultipartBody.Part): String {
        return retrofitSource.myTest(file)
    }


}