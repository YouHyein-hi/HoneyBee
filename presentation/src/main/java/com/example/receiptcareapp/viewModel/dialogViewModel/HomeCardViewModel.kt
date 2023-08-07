package com.example.receiptcareapp.viewModel.dialogViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.UpdateCardData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpdateCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.UpdateCardUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.util.ResponseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

class HomeCardViewModel @Inject constructor(
    private val getCardListUseCase: GetCardListUseCase,
    private val updateCardUseCase: UpdateCardUseCase
) : BaseViewModel(){

    init { Log.e("TAG", "HomeCardBottomSheetViewModel", ) }

    val loading : MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response : LiveData<ResponseState> get() = _response


    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        CoroutineScope(exceptionHandler).launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                getCardListUseCase()
            } ?:throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun updateServerCardData(updateCardData : UpdateCardData) {
        Log.e("TAG", "updateCardData: $updateCardData",)
        CoroutineScope(exceptionHandler).launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                updateResponse(
                    updateCardUseCase(
                        DomainUpdateCardData(
                            id = updateCardData.id,
                            cardName = updateCardData.cardName,
                            cardAmount = updateCardData.cardAmount
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    private fun updateResponse(response: DomainServerReponse){
        when(response.status){
            "200" -> {
                getServerCardData()
                _response.postValue(ResponseState.SUCCESS)
            }
            else -> {_response.postValue(ResponseState.FALSE)}
        }
    }
}