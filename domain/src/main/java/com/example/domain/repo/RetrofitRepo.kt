package com.example.domain.repo

import android.graphics.Bitmap
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainResendAllData
import com.example.domain.model.receive.DomainResendCardData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.model.send.DomainSendData
import okhttp3.MultipartBody

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitRepo {
    suspend fun sendDataRepo(domainSendData: DomainSendData): String

    suspend fun receiveDataRepo():MutableList<DomainReceiveAllData>

    suspend fun receiveCardDataRepo():MutableList<DomainReceiveCardData>

    suspend fun receivePictureDataRepo(uid:String):Bitmap

    suspend fun deleteServerData(id: Long):String

    suspend fun sendCardDataRepo(dataDomainSendCardData: DomainSendCardData): String

    suspend fun deleteCardDataRepo(id: Long):String

    suspend fun updateDataRepo(domainResendData: DomainResendAllData):String

    suspend fun updateCardDataRepo(domainResendCardData: DomainResendCardData):String

    suspend fun myTest(file:MultipartBody.Part):String
}