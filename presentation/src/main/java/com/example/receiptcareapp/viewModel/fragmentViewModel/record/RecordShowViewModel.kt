package com.example.receiptcareapp.viewModel.fragmentViewModel.record

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.UpdateData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.card.DomainUpadateData
import com.example.domain.model.receive.card.ServerCardSpinnerData
import com.example.domain.model.receive.ServerUidData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.bill.DeleteDataUseCase
import com.example.domain.usecase.bill.GetPictureDataUseCase
import com.example.domain.usecase.bill.InsertDataUseCase
import com.example.domain.usecase.bill.UpdateDataUseCase
import com.example.domain.usecase.card.GetCardSpinnerUseCase
import com.example.domain.usecase.room.DeleteDataRoomUseCase
import com.example.domain.usecase.room.UpdateRoomData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.dto.LocalBillData
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.util.UriToBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-06-21
 * pureum
 */
@HiltViewModel
class RecordShowViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val getPictureDataUseCase: GetPictureDataUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    private val getCardSpinnerUseCase: GetCardSpinnerUseCase,
    private val insertDataUseCase: InsertDataUseCase,
    private val deleteDataRoomUseCase: DeleteDataRoomUseCase,
    private val updateRoomData: UpdateRoomData
    ) : BaseViewModel("RecordShowViewModel") {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<Pair<ResponseState,ServerUidData?>>()
    val response : LiveData<Pair<ResponseState,ServerUidData?>> get() = _response

    // 서버 카드 전달받은 값 관리
    private var _cardList = MutableLiveData<ServerCardSpinnerData?>()
    val cardList: LiveData<ServerCardSpinnerData?>
        get() = _cardList

    private var _picture = MutableLiveData<Bitmap?>()
    val picture : LiveData<Bitmap?> get(){
        return _picture
    }

    private lateinit var savedServerData: UpdateData
    private lateinit var savedLocalData: LocalBillData


    // TODO ChangeDialog에만 들어가는 코드인데 ChangeViewModel에 옮길까
    //서버 데이터 업데이트
    fun updateServerBillData(sendData: UpdateData, uid: String) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(
                    Pair(
                        ResponseState.UPDATE_SUCCESS,
                        updateDataUseCase(
                            DomainUpadateData(
                                id = uid.toLong(),
                                cardName = sendData.cardName,
                                storeName = sendData.storeName,
                                billSubmitTime = LocalDateTime.parse(sendData.billSubmitTime),
                                amount = sendData.amount.replace(",", "").toInt()
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
                            DomainSendData(
                                cardName = MultipartBody.Part.createFormData(
                                    "cardName",
                                    sendData.cardName
                                ),
                                storeName = MultipartBody.Part.createFormData(
                                    "storeName",
                                    sendData.storeName
                                ),
                                date = MultipartBody.Part.createFormData(
                                    "billSubmitTime",
                                    sendData.billSubmitTime
                                ),
                                amount = MultipartBody.Part.createFormData(
                                    "amount",
                                    sendData.amount.replace(",", "")
                                ),
                                picture = UriToBitmap(application).compressEncodePicture(sendData.picture)
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

    fun deleteRoomBillData(date: String? = savedLocalData.billSubmitTime) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)
            deleteDataRoomUseCase(date!!)
            isLoading.postValue(false)
        }

    }

    fun upDataRoomData(){
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
            updateRoomData(
                DomainRoomData(
                    uid = savedLocalData.uid,
                    cardName = savedLocalData.cardName,
                    amount = savedLocalData.amount,
                    billSubmitTime = savedLocalData.billSubmitTime,
                    storeName = savedLocalData.storeName,
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

    fun getServerPictureData(uid:String){
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                loading.postValue(true)
                _picture.postValue(getPictureDataUseCase(uid).picture)
                loading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }
}