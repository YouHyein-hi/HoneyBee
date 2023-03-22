package com.example.domain.repo

import com.example.domain.model.DomainReceiveAllData
import okhttp3.MultipartBody

/**
 * 2023-02-02
 * pureum
 */
interface RetrofitRepo {
    suspend fun sendDataRepo(card:MultipartBody.Part, amount:MultipartBody.Part, pictureName:MultipartBody.Part, date:MultipartBody.Part, picture: MultipartBody.Part): String

    suspend fun receiveDataRepo():MutableList<DomainReceiveAllData>

    suspend fun deleteServerData(date:String):String
}