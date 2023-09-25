package com.example.receiptcareapp.viewModel.fragmentViewModel.record

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.local.RoomData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.receive.bill.DetailBillData
import com.example.domain.model.remote.send.bill.SendBillUpdateData
import com.example.domain.usecase.card.GetCardSpinnerUseCase
import com.example.domain.usecase.room.DeleteRoomDataUseCase
import com.example.domain.usecase.room.UpdateRoomDataUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.domain.model.ui.bill.LocalBillData
import com.example.domain.model.ui.bill.UiBillData
import com.example.domain.usecase.bill.*
import com.example.receiptcareapp.state.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class RecordShowViewModel @Inject constructor(
    private val getPictureDataUseCase: GetPictureDataUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    private val getCardSpinnerUseCase: GetCardSpinnerUseCase,
    private val insertDataUseCase: InsertDataUseCase,
    private val deleteRoomDataUseCase: DeleteRoomDataUseCase,
    private val updateRoomDataUseCase: UpdateRoomDataUseCase,
    private val getDetailDataUseCase: GetDetailDataUseCase,
    private val getBillCheckUseCase: BillCheckUseCase
) : BaseViewModel("RecordShowViewModel") {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<Pair<ResponseState, ServerUidData?>>()
    val response : LiveData<Pair<ResponseState, ServerUidData?>> get() = _response

    private var _cardList = MutableLiveData<ServerCardSpinnerData?>()
    val cardList: LiveData<ServerCardSpinnerData?> get() = _cardList

    private var _picture = MutableLiveData<Bitmap?>()
    val picture : LiveData<Bitmap?> get() = _picture

    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri> get() = _image
    fun takeImage(img: Uri) { _image.value = img }

    private var _changePicture = MutableLiveData<Bitmap?>()
    val changePicture : LiveData<Bitmap?> get() = _changePicture
    fun takeChangePicture(pic: Bitmap) { _changePicture.value = pic }

    private var _check = MutableLiveData<Boolean>()
    val check: LiveData<Boolean> get() = _check

    private var _serverInitData = MutableLiveData<Pair<Bitmap?, DetailBillData>>()
    val serverInitData : LiveData<Pair<Bitmap?, DetailBillData>> get() = _serverInitData

    var textValue: String? = null

    private lateinit var savedServerData: SendBillUpdateData
    private lateinit var savedLocalData: LocalBillData

    fun updateServerBillData(sendData: SendBillUpdateData) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(
                    Pair(
                        ResponseState.UPDATE_SUCCESS,
                        updateDataUseCase(
                            SendBillUpdateData(
                                id = sendData.id,
                                cardName = sendData.cardName,
                                storeName = sendData.storeName,
                                billSubmitTime = sendData.billSubmitTime,
                                storeAmount = sendData.storeAmount,
                                billCheck = sendData.billCheck,
                                billMemo = sendData.billMemo
                            )
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            savedServerData = sendData
            isLoading.postValue(false)
        }
    }

    fun updateLocalBillData(sendData: LocalBillData) {
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                _response.postValue(Pair(
                    ResponseState.LOCAL_UPDATE_SUCCESS,
                    insertDataUseCase(
                        UiBillData(
                            cardName = sendData.cardName,
                            storeName = sendData.storeName,
                            date = sendData.date,
                            storeAmount = sendData.storeAmount.replace(",", ""),
                            picture = sendData.picture,
                            memo = sendData.memo
                        )
                    )
                )
                )
            } ?: throw SocketTimeoutException()
            savedLocalData = sendData
        }
    }

    fun deleteServerBillData(id: Long) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.value = Pair(ResponseState.DELETE_SUCCESS, deleteDataUseCase(id))
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun BillCheckData(id: Long) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                getBillCheckUseCase(id)
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun deleteRoomBillData(date: String? = savedLocalData.date) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)
            deleteRoomDataUseCase(date!!)
            isLoading.postValue(false)
        }
    }

    fun upDataRoomData(){
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
            updateRoomDataUseCase(
                RoomData(
                    uid = savedLocalData.uid,
                    cardName = savedLocalData.cardName,
                    storeAmount = savedLocalData.storeAmount,
                    billSubmitTime = savedLocalData.date,
                    storeName = savedLocalData.storeName,
                    memo = savedLocalData.memo,
                    file = savedLocalData.picture.toString()
                )
            )
        }
    }

    fun getServerCardData() {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardList.postValue(getCardSpinnerUseCase())
                isLoading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }

    fun getServerInitData(id: String) {
        modelScope.launch {
            loading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _serverInitData.postValue(
                    Pair(
                        getPictureDataUseCase(id).picture,
                        getDetailDataUseCase(id).body!!
                    )
                )
            }
            loading.postValue(false)
        }
    }
}