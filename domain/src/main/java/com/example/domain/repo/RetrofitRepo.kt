package com.example.domain.repo

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitRepo {
    suspend fun sendDataRepo(card:String, amount:Int, pictureName:String, date:LocalDateTime, bill: MultipartBody.Part): DomainSendData

    suspend fun receiveDataRepo():DomainReceiveData

    suspend fun test():String
}