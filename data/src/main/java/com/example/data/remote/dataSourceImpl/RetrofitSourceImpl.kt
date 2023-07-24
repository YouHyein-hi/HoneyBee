//package com.example.data.remote.dataSourceImpl
//
//import android.util.Log
//import com.example.data.remote.dataSource.RetrofitSource
//import com.example.data.remote.model.ServerResponse
//import com.example.data.remote.model.ReceiveCardData
//import com.example.data.remote.model.ReceiveData
//import okhttp3.MultipartBody
//import retrofit2.Retrofit
//import java.time.LocalDateTime
//import javax.inject.Inject
//
///**
// * 2023-02-02
// * pureum
// */
//class RetrofitSourceImpl @Inject constructor(
//    private val retrofit: Retrofit,
//) : RetrofitSource {
//
//
//    override suspend fun sendDataSource(
//        cardName: MultipartBody.Part,
//        amount: MultipartBody.Part,
//        pictureName: MultipartBody.Part,
//        timestmap: MultipartBody.Part,
//        bill: MultipartBody.Part
//    ): String {
//        return retrofit.create(RetrofitSource::class.java).sendDataSource(
//            cardName = cardName,
//            amount = amount,
//            storeName = pictureName,
//            billSubmitTime = timestmap,
//            file = bill
//        )
//    }
//
//    override suspend fun sendCardDataSource(
//        cardName: String?,
//        amount: Int?
//    ): String {
//        return retrofit.create(RetrofitSource::class.java).sendCardDataSource(cardName = cardName, amount = amount)
//    }
//
//    override suspend fun receiveDataSource(): MutableList<ReceiveData> {
//        return retrofit.create(RetrofitSource::class.java).receiveDataSource()
//    }
//
//    override suspend fun receiveCardDataSource(): MutableList<ReceiveCardData> {
//        return retrofit.create(RetrofitSource::class.java).receiveCardDataSource()
//    }
//
//    override suspend fun receivePictureDataSource(uid:String): String {
//        return retrofit.create(RetrofitSource::class.java).receivePictureDataSource(uid)
//    }
//
//    override suspend fun deleteServerData(uid:Long): String {
//        return retrofit.create(RetrofitSource::class.java).deleteServerData(uid)
//    }
//
//    override suspend fun deleteCardDataSource(uid:Long): String {
//        return retrofit.create(RetrofitSource::class.java).deleteCardDataSource(uid)
//    }
//
//    override suspend fun updateDataSource(
//        id: Long,
//        cardName: String,
//        storeName: String,
//        billSubmitTime: LocalDateTime,
//        amount: String
//    ): ServerResponse {
//        return retrofit.create(RetrofitSource::class.java).updateDataSource(
//            id = id,
//            cardName = cardName,
//            amount = amount,
//            storeName = storeName,
//            billSubmitTime = billSubmitTime,
//        )
//    }
//
//    override suspend fun updateCardDataSource(
//        id : Long, cardName: String, cardAmount: Int
//    ): ServerResponse {
//        return retrofit.create(RetrofitSource::class.java).updateCardDataSource(
//            id = id,
//            cardName = cardName,
//            cardAmount = cardAmount
//        )
//    }
//
//    override suspend fun requestLogin(email: String, password: String): ServerResponse {
//        return retrofit.create(RetrofitSource::class.java).requestLogin(
//            email = email,
//            password = password
//        )
//    }
//
//
//    override suspend fun myTest(file: MultipartBody.Part): String {
//        Log.e("TAG", "myTest: test $file")
//        return retrofit.create(RetrofitSource::class.java).myTest(file)
//    }
//}