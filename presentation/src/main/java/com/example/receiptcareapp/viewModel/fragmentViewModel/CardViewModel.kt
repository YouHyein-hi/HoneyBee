package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.receive.CardResponseData
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.send.AppSendCardData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.InsertCardUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 2023-08-20
 * pureum
 */
@HiltViewModel
class CardViewModel @Inject constructor(
    private val insertCardUseCase: InsertCardUseCase,
    private val getCardListUseCase: GetCardListUseCase
) : BaseViewModel() {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _cardList = MutableLiveData<CardResponseData?>()
    val cardList: LiveData<CardResponseData?> get() = _cardList

    private var _response = MutableLiveData<ServerResponseData?>()
    val response: LiveData<ServerResponseData?> get() = _response

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardList.postValue(getCardListUseCase())
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun insertServerCardData(sendData: AppSendCardData) {
        Log.e("TAG", "sendCardData: 카드 보내기 $sendData")
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(
                    insertCardUseCase(
                        DomainSendCardData(
                            cardName = sendData.cardName,
                            cardAmount = sendData.cardAmount,
                            billCheckDate = sendData.billCheckDate
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }
}