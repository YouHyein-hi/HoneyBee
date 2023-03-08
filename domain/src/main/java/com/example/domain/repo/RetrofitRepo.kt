package com.example.domain.repo

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitRepo {
    suspend fun sendDataRepo(date: LocalDateTime, amount : Int, card:String, picture: MultipartBody.Part): DomainSendData

    suspend fun receiveDataRepo():DomainReceiveData
}