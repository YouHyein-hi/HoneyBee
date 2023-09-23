package com.example.receiptcareapp.viewModel.fragmentViewModel.record

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.local.RoomData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.basic.ServerUidData
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

/**
 * 2023-06-21
 * pureum
 */
@HiltViewModel
class RecordShowViewModel @Inject constructor(
    private val getPictureDataUseCase: GetPictureDataUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    private val getCardSpinnerUseCase: GetCardSpinnerUseCase,
    private val insertDataUseCase: InsertDataUseCase,
    private val deleteRoomDataUseCase: DeleteRoomDataUseCase,
    private val updateRoomDataUseCase: UpdateRoomDataUseCase,
    private val getDetailDataUseCase: GetDetailDataUseCase
    ) : BaseViewModel("RecordShowViewModel") {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<Pair<ResponseState, ServerUidData?>>()
    val response : LiveData<Pair<ResponseState, ServerUidData?>> get() = _response

    // 서버 카드 전달받은 값 관리
    private var _cardList = MutableLiveData<ServerCardSpinnerData?>()
    val cardList: LiveData<ServerCardSpinnerData?>
        get() = _cardList

    private var _picture = MutableLiveData<Bitmap?>()
    val picture : LiveData<Bitmap?> get(){
        return _picture
    }

    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri) { _image.value = img }

    private var _changePicture = MutableLiveData<Bitmap?>()
    val changePicture : LiveData<Bitmap?> get(){
        return _changePicture
    }
    fun takeChangePicture(pic: Bitmap) { _changePicture.value = pic }

    var textValue: String? = null

    private lateinit var savedServerData: SendBillUpdateData
    private lateinit var savedLocalData: LocalBillData


    // TODO ChangeDialog에만 들어가는 코드인데 ChangeViewModel에 옮길까
    //서버 데이터 업데이트
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
//                                billSubmitTime = LocalDateTime.parse(sendData.billSubmitTime),
                                date = sendData.date,
                                storeAmount = sendData.storeAmount
                            )
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            savedServerData = sendData
            isLoading.postValue(false)
        }
    }

    //로컬 데이터 재전송
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

    fun getServerPictureData(id:String){
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                loading.postValue(true)
                _picture.postValue(getPictureDataUseCase(id).picture)
                loading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }

    fun getDetailBillData(id: String){
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                Log.e("TAG", "getDetailBillData: start", )
                Log.e("TAG", "getDetailBillData: ${getDetailDataUseCase(id)}", )
            }?:throw SocketTimeoutException()
        }
    }

    fun billCheckComplete(){

    }
    fun billCheckCancel(){}
}