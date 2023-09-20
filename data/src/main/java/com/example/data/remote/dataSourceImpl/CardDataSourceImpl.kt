package com.example.data.remote.dataSourceImpl

import ServerResponse
import android.util.Log
import com.example.data.di.RetrofitModule
import com.example.data.remote.dataSource.CardDataSource
import com.example.data.remote.model.ServerCardResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import java.time.LocalDate
import javax.inject.Inject


class CardDataSourceImpl @Inject constructor(
    @RetrofitModule.Api private val retrofit: Retrofit
): CardDataSource {
    override suspend fun sendCardDataSource(
        cardName: String,
        amount: Int,
        expireDate: LocalDate,
        designId: Int
    ): Response<ServerResponse<String>> {
        retrofit.create(CardDataSource::class.java).sendCardDataSource(cardName = cardName, amount = amount, expireDate = expireDate, designId = designId).let {
            if(it.isSuccessful)
                return it
            else
                when(it.code()){
                    400 -> throw Exception("이미 존재하는 카드입니다.")
                    else -> throw Exception("서버 통신 오류")

                }

        }
    }

    override suspend fun getCardDataSource(): ServerResponse<List<ServerCardResponse>> {

        return retrofit.create(CardDataSource::class.java).getCardDataSource().also {
            Log.e(
                "TAG",
                "getCardDataSource: $it",
            ) }
    }

    override suspend fun deleteCardDataSource(id: Long): ServerResponse<Int> {
        return retrofit.create(CardDataSource::class.java).deleteCardDataSource(id)
    }

    override suspend fun updateCardDataSource(
        id: Long,
        cardName: String,
        cardAmount: Int,
        cardExpireDate: LocalDate,
        cardDesignId: Int
    ): ServerResponse<Int> {
        return retrofit.create(CardDataSource::class.java).updateCardDataSource(
            id = id, cardName = cardName, cardAmount = cardAmount, cardExpireDate = cardExpireDate, cardDesignId = cardDesignId
        )
    }
}