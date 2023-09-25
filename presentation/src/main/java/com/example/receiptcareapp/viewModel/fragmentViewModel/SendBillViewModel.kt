package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.local.RoomData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.bill.ServerStoreData
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.ui.bill.UiBillData
import com.example.domain.usecase.bill.GetStoreListUseCase
import com.example.domain.usecase.bill.InsertDataUseCase
import com.example.domain.usecase.card.GetCardSpinnerUseCase
import com.example.domain.usecase.room.InsertRoomDataUseCase
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SendBillViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val insertDataUseCase: InsertDataUseCase,
    private val insertRoomDataUseCase: InsertRoomDataUseCase,
    private val getStoreListUseCase: GetStoreListUseCase,
    private val getCardSpinnerUseCase: GetCardSpinnerUseCase,
) : BaseViewModel("SendBillViewModel") {

    val loading: LiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ServerUidData?>()
    val response : LiveData<ServerUidData?> get() = _response

    private lateinit var savedData : UiBillData

    //서버 응답 일관화 이전에 사용할 박스

    //서버 응답 일관화 이전에 사용할 박스
    private var _storeList = MutableLiveData<ServerStoreData?>()
    val storeList : LiveData<ServerStoreData?> get() = _storeList

    private var _cardList = MutableLiveData<ServerCardSpinnerData?>()
    val cardList : LiveData<ServerCardSpinnerData?> get() = _cardList

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                isLoading.value = true
                _cardList.postValue(getCardSpinnerUseCase())
                isLoading.value = false
            }?:throw SocketTimeoutException()
        }
    }

    fun getServerStoreData(){
        modelScope.launch {
            withTimeoutOrNull(waitTime){
                isLoading.value = true
                _storeList.postValue(getStoreListUseCase())
                isLoading.value = false
            }?:throw SocketTimeoutException()
        }
    }

    fun insertBillData(data: UiBillData) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(
                    insertDataUseCase(
                        UiBillData(
                            cardName = data.cardName,
                            storeName = data.storeName,
                            date = data.date,
                            storeAmount = data.storeAmount.replace(",", ""),
                            picture = data.picture,
                            memo = data.memo
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            savedData = data
            isLoading.postValue(false)
        }
    }

    fun insertRoomData(response:String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            insertRoomDataUseCase(
                RoomData(
                    cardName = savedData.cardName,
                    storeAmount = savedData.storeAmount,
                    storeName = savedData.storeName,
                    billSubmitTime = savedData.date,
                    memo = savedData.memo,
                    file = savedData.picture.toString(),
                    uid = response
                )
            )
        }
    }
}