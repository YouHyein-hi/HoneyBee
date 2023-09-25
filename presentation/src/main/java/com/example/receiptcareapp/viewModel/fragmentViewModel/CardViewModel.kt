package com.example.receiptcareapp.viewModel.fragmentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.receive.card.ServerCardDetailData
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.model.remote.send.card.SendUpdateCardData
import com.example.domain.usecase.card.*
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class CardViewModel @Inject constructor(
    private val insertCardUseCase: InsertCardUseCase,
    private val getCardListUseCase: GetCardListUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val getCardDetailUseCase: GetCardDetailUseCase
) : BaseViewModel("CardViewModel") {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _cardList = MutableLiveData<ServerCardData?>()
    val cardList: LiveData<ServerCardData?> get() = _cardList

    private var _cardDetailList = MutableLiveData<ServerCardDetailData?>()
    val cardDetailList: LiveData<ServerCardDetailData?> get() = _cardDetailList

    private var _response = MutableLiveData<ServerResponseData?>()
    val response: LiveData<ServerResponseData?> get() = _response

    private var _id = MutableLiveData<Long>()
    val id : LiveData<Long> get() = _id
    fun putId(id : Long){
        _id.value = id
    }

    var textValue: String? = null

    fun getServerCardData() {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardList.postValue(getCardListUseCase())
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun getServerCardDetailData(id: String){
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardDetailList.postValue(getCardDetailUseCase(id))
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
                            cardExpireDate = sendData.cardExpireDate,
                            cardDesignId = sendData.cardDesignId
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun deleteServerCardDate(id: Long){
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                deleteCardUseCase(id)
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun updateServerCardDate(sendUpdateCardData: SendUpdateCardData){
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                updateCardUseCase(
                    SendUpdateCardData(
                        id = sendUpdateCardData.id,
                        cardName = sendUpdateCardData.cardName,
                        cardAmount = sendUpdateCardData.cardAmount,
                        cardExpireDate = sendUpdateCardData.cardExpireDate,
                        cardDesignId = sendUpdateCardData.cardDesignId
                    )
                )
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

}