package com.example.data.repoImpl

import com.example.data.remote.dataSource.RetrofitSource
import com.example.data.remote.dto.toDomainReceiveData
import com.example.data.remote.dto.toDomainSendData
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.RetrofitRepo
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDateTime
import javax.inject.Inject


/**
 * 2023-02-02
 * pureum
 */
class RetrofitRepoImpl @Inject constructor(
    private val retrofitSource: RetrofitSource,
):RetrofitRepo{
    override suspend fun sendDataRepo(card:String, amount:Int, pictureName:String, date:LocalDateTime, bill:MultipartBody.Part): DomainSendData {

        var myCard = MultipartBody.Part.createFormData("card",card)
        var myAmount = MultipartBody.Part.createFormData("amount",amount.toString())
        var myPicture = MultipartBody.Part.createFormData("pictureName","pictureName")
        var myDate = MultipartBody.Part.createFormData("date",date.toString())

        return retrofitSource.sendDataSource(cardName = myCard, amount = myAmount, pictureName=myPicture, timestmap = myDate, bill = bill).toDomainSendData()
    }

    override suspend fun receiveDataRepo(): DomainReceiveData {
        return retrofitSource.receiveDataSource().toDomainReceiveData()
    }

    override suspend fun test(): String {
        return retrofitSource.test("ㅇ")
    }


}