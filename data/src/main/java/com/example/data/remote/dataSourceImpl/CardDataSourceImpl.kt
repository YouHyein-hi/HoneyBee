package com.example.data.remote.dataSourceImpl

import ServerResponse
import android.util.Log
import com.example.data.di.RetrofitModule
import com.example.data.remote.dataSource.CardDataSource
import com.example.data.remote.dataSource.LoginDataSource
import com.example.data.remote.model.ServerCardResponse
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class CardDataSourceImpl @Inject constructor(
    @RetrofitModule.Api private val retrofit: Retrofit
): CardDataSource {
    override suspend fun sendCardDataSource(
        cardName: String,
        amount: Int,
        billCheckDate: String
    ): ServerResponse<String> {
        return retrofit.create(CardDataSource::class.java).sendCardDataSource(cardName = cardName, amount = amount, billCheckDate = billCheckDate)
    }

    override suspend fun getCardDataSource(): ServerResponse<List<ServerCardResponse>> {

        return retrofit.create(CardDataSource::class.java).getCardDataSource().also {
            Log.e(
                "TAG",
                "getCardDataSource: $it",
            ) }
    }

    //아직 안쓰이는 기능
//    override suspend fun deleteCardDataSource(uid: Long): String {
//        return retrofit.create(CardDataSource::class.java).deleteCardDataSource(uid)
//    }
//
//    override suspend fun updateCardDataSource(
//        id: Long,
//        cardName: String,
//        cardAmount: Int
//    ): ServerResponse {
//        return retrofit.create(CardDataSource::class.java).updateCardDataSource(
//            id = id,
//            cardName = cardName,
//            cardAmount = cardAmount,
//        )
//    }
}