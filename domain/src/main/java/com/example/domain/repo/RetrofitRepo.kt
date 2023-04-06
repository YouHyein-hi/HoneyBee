package com.example.domain.repo

import com.example.domain.model.*
import okhttp3.MultipartBody

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitRepo {
    suspend fun sendDataRepo(domainSendData: DomainSendData): String

    suspend fun receiveDataRepo():MutableList<DomainReceiveAllData>

    suspend fun deleteServerData(id: Long):String

    suspend fun sendCardDataRepo(dataDomainSendCardData: DomainSendCardData): String

    suspend fun receiveCardDataRepo():MutableList<DomainReceiveCardData>

    suspend fun deleteCardDataRepo(id: Long):String

    suspend fun resendDataRepo(domainResendData: DomainResendData):String

    suspend fun resendCardDataRepo(domainResendCardData: DomainResendCardData):String

    suspend fun myTest(file:MultipartBody.Part):String
}