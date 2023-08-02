package com.example.receiptcareapp.viewModel.dialogViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.model.UpdateCardData
import com.example.domain.model.receive.DomainUpdateCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.UpdateCardUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

class HomeCardViewModel @Inject constructor(
    application: Application,
    private val getCardListUseCase: GetCardListUseCase,
    private val updateCardUseCase: UpdateCardUseCase
) : BaseViewModel(application){

    init { Log.e("TAG", "HomeCardBottomSheetViewModel", ) }

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = getCardListUseCase()
                Log.e("TAG", "receiveCardData: $gap")
                _cardData.postValue(gap)
            } ?:throw SocketTimeoutException()
        }
    }

    fun updateServerCardData(updateCardData : UpdateCardData){
        Log.e("TAG", "updateCardData: ${updateCardData}", )
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "updateCardData: 훔냠냠", )
            withTimeoutOrNull(waitTime) {
                val result = updateCardUseCase(
                    DomainUpdateCardData(
                        id = updateCardData.id,
                        cardName = updateCardData.cardName,
                        cardAmount = updateCardData.cardAmount
                    )
                )
                Log.e("TAG", "updateCardData result: ${result}",)

                if(result.status == "200"){
                    //TODO 이런 유기적인 연결 다 지우기
                    getServerCardData()
                }
                else{
                    Exception("오류! 전송 실패")
                }


            }?:throw SocketTimeoutException()
        }
//        retrofitUseCase.updateCardDateUseCase()
    }
}