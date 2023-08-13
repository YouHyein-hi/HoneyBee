package com.example.receiptcareapp.viewModel.dialogViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.send.AppSendCardData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.InsertCardUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.util.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CardAddViewModel @Inject constructor(
    private val insertCardUseCase: InsertCardUseCase,
    private val getCardListUseCase: GetCardListUseCase,
) : BaseViewModel(){

    init { Log.e("TAG", "CardAddBottomViewModel", ) }

    private var _response = MutableLiveData<ResponseState>()
    val response : LiveData<ResponseState> get() = _response

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

    fun PriceFormat(price : String): String? {
        return DecimalFormat("#,###").format(price.toInt())
    }

    fun insertServerCardData(sendData: AppSendCardData) {
        Log.e("TAG", "sendCardData: 카드 보내기 $sendData",)
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                updateResponse(
                    insertCardUseCase(
                        DomainSendCardData(
                            cardName = sendData.cardName,
                            cardAmount = sendData.cardAmount
                        )
                    ),
                    ResponseState.UPDATE_SUCCESS
                )
                //TODO 이런 유기적인 관계로 걸려있으면 안됨
            } ?: throw SocketTimeoutException()
        }
    }

    fun updateResponse(response:DomainServerReponse, type: ResponseState){
        when(response.status){
            "200" ->{
                _response.postValue(type)
            }
            else -> {}
        }
    }
}