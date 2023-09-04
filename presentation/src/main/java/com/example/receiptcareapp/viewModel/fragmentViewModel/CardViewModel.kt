package com.example.receiptcareapp.viewModel.fragmentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.InsertCardUseCase
import com.example.receiptcareapp.base.BaseViewModel
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
) : BaseViewModel("CardViewModel") {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _cardList = MutableLiveData<ServerCardData?>()
    val cardList: LiveData<ServerCardData?> get() = _cardList

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

    fun insertServerCardData(sendData: SendCardData) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(
                    insertCardUseCase(
                        SendCardData(
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