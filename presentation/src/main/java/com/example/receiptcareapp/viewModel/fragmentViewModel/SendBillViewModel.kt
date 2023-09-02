package com.example.receiptcareapp.viewModel.fragmentViewModel

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
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.card.ServerCardSpinnerData
import com.example.domain.model.receive.bill.ServerStoreData
import com.example.domain.model.receive.ServerUidData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.bill.GetStoreListUseCase
import com.example.domain.usecase.bill.InsertDataUseCase
import com.example.domain.usecase.card.GetCardSpinnerUseCase
import com.example.domain.usecase.room.InsertDataRoomUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.util.UriToBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SendBillViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val insertDataUseCase: InsertDataUseCase,
    private val insertDataRoomUseCase: InsertDataRoomUseCase,
    private val getStoreListUseCase: GetStoreListUseCase,
    private val getCardSpinnerUseCase: GetCardSpinnerUseCase
) : BaseViewModel("SendBillViewModel") {

    val loading: LiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ServerUidData?>()
    val response : LiveData<ServerUidData?> get() = _response

    private lateinit var savedData : AppSendData

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
    //insertData 분리해야함
    fun insertBillData(sendData: AppSendData) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(
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
            } ?: throw SocketTimeoutException()
            savedData = sendData
            isLoading.postValue(false)
        }
    }

    fun insertRoomData(response:String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            insertDataRoomUseCase(
                DomainRoomData(
                    cardName = savedData.cardName,
                    amount = savedData.amount,
                    storeName = savedData.storeName,
                    billSubmitTime = savedData.billSubmitTime,
                    file = savedData.picture.toString(),
                    uid = response
                )
            )
        }
    }

}