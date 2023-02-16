package com.example.domain.repo

import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitRepo {
    suspend fun sendDataRepo(card:String, date:String, picture:ByteArray): DomainSendData

    suspend fun receiveDataRepo():DomainReceiveData
}