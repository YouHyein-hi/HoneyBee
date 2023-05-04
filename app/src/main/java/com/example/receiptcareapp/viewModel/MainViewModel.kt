package com.example.receiptcareapp.viewModel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.AppSendCardData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.RetrofitUseCase
import com.example.domain.usecase.RoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val retrofitUseCase: RetrofitUseCase,
    private val roomUseCase: RoomUseCase
) : BaseViewModel() {

    private var waitTime = 3000L
    //이렇게 쓰면 메모리 누수가 일어난다는데 왜??
    var myCotext: Context? = null

    //서버에서 받은 데이터 담는 박스
    private val _serverData = MutableLiveData<MutableList<DomainReceiveAllData>>()
    val serverData: LiveData<MutableList<DomainReceiveAllData>>
        get() = _serverData

    //룸에서 받은 데이터 담는 박스
    private var _roomData = MutableLiveData<MutableList<DomainRoomData>>()
    val roomData: LiveData<MutableList<DomainRoomData>>
        get() = _roomData

    //서버 연결 유무 관리
    private var _connectedState = MutableLiveData<ConnectedState>()
    val connectedState: LiveData<ConnectedState>
        get() = _connectedState
    fun changeConnectedState(connectedState: ConnectedState) {
        Log.e("TAG", "changeConnectedState: $connectedState")
        _connectedState.value = connectedState
    }

    // 서버 카드 전달받은 값 관리
    private var _cardData = MutableLiveData<MutableList<DomainReceiveCardData>>()
    val cardData: LiveData<MutableList<DomainReceiveCardData>>
        get() = _cardData

    // 코루틴 값을 담아두고 원할때 취소하기
    private var _serverJob = MutableLiveData<Job>()

    //서버에 데이터 전송 기능
    fun sendData(sendData: AppSendData) {
        Log.e("TAG", "sendData: $sendData", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val file = File(absolutelyPath(sendData.picture, myCotext))
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val myPicture = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val result = retrofitUseCase.sendDataUseCase(
                    DomainSendData(
                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
                        storeName = MultipartBody.Part.createFormData(
                            "storeName",
                            sendData.storeName
                        ),
                        date = MultipartBody.Part.createFormData("date", sendData.date),
                        amount = MultipartBody.Part.createFormData("amount", sendData.amount),
                        picture = myPicture
                    )
                )
                Log.e("TAG", "sendData 응답 : $result ")

                if (result == "add success") {
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
                    insertRoomData(
                        DomainRoomData(
                            cardName = sendData.cardName,
                            amount = sendData.amount,
                            storeName = sendData.storeName,
                            date = sendData.date,
                            file = sendData.picture.toString()
                        )
                    )
                } else {
                    Log.e("TAG", "sendData: 실패입니다!")
                    _connectedState.postValue(ConnectedState.CONNECTING_FALSE)
                    Exception("오류! 전송 실패.")
                }
            } ?: throw SocketTimeoutException()
        }
    }

    //절대경로로 변환
    fun absolutelyPath(path: Uri?, context: Context?): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context?.contentResolver?.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)
        return result!!
    }

    fun sendCardData(sendData: AppSendCardData) {
        Log.e("TAG", "sendCardData: 카드 보내기 $sendData", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                retrofitUseCase.sendCardDataUseCase(
                    DomainSendCardData(
                        cardName = sendData.cardName,
                        cardAmount = sendData.cardAmount
//                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
//                        cardAmount = MultipartBody.Part.createFormData(
//                            "cardName",
//                            sendData.cardAmount
//                        )
                    )
                )
                _connectedState.postValue(ConnectedState.CARD_CONNECTING_SUCCESS)
                // 성공하면 값을 불러오기
                receiveServerCardData()
            }?:throw SocketTimeoutException()
        }
    }

    fun receiveServerAllData() {
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime){
                val gap = retrofitUseCase.receiveDataUseCase()
                Log.e("TAG", "receiveAllServerData: $gap")
                _serverData.postValue(gap)
                _connectedState.postValue(ConnectedState.DISCONNECTED)
            } ?: throw SocketTimeoutException()

        }
    }

    fun receiveServerCardData() {
        _connectedState.postValue(ConnectedState.CONNECTING)
        _serverJob.postValue(CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = retrofitUseCase.receiveCardDataUseCase()
                Log.e("TAG", "receiveCardData: $gap")
                _cardData.postValue(gap)
//            _cardData.value = retrofitUseCase.receiveCardDataUseCase()
                // 통신 끝나면 커넥트 풀어주기
                _connectedState.postValue(ConnectedState.DISCONNECTED)
                // 라이브데이터로 관리하기!
                // 결과값을 분기문으로 관리 + 커넥트 풀어주기
            }?:throw SocketTimeoutException()
        })
    }

    fun deleteCardData(id: Long) {
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                retrofitUseCase.deleteCardDataUseCase(id)
                // 결과값을 분기문으로 관리 + 커넥트 풀어주기
                // 성공하면 값을 불러오기
                receiveServerCardData()
            }?:throw SocketTimeoutException()
        }
    }

    fun deleteServerData(id: Long) {
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = retrofitUseCase.deleteServerData(id)
                Log.e("TAG", "deleteServerData return: $gap")
            }?: throw SocketTimeoutException()
        }
    }

    fun changeServerData(sendData: AppSendData) {
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = roomUseCase.getAllData()
                Log.e("TAG", "changeServerData: $gap")
//            retrofitUseCase.resendDataUseCase(DomainSendData(sendData.))
            }?:throw SocketTimeoutException()
        }
    }

    fun changeCardData(id: Long) {
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = roomUseCase.getAllData()
                Log.e("TAG", "changeCardData: $gap")
//            retrofitUseCase.resendCardDataUseCase()
            }?:throw SocketTimeoutException()
        }
    }

    fun deleteRoomData(date: String) {
        CoroutineScope(exceptionHandler).launch {
            roomUseCase.deleteData(date)
            //삭제 후에 데이터 끌어오기 위한 구성
            receiveAllRoomData()
        }
    }

    fun insertRoomData(domainRoomData: DomainRoomData) {
        CoroutineScope(exceptionHandler).launch {
            roomUseCase.insertData(
                DomainRoomData(
                    cardName = domainRoomData.cardName,
                    amount = domainRoomData.amount,
                    storeName = domainRoomData.storeName,
                    date = domainRoomData.date,
                    file = domainRoomData.file
                )
            )
            _connectedState.postValue(ConnectedState.CONNECTING)
        }
    }

    fun receiveAllRoomData() {
        CoroutineScope(exceptionHandler).launch {
            val gap = roomUseCase.getAllData()
            Log.e("TAG", "receiveAllRoomData: $gap")
            _roomData.postValue(gap)
        }
    }


    fun serverCoroutineStop() {
        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소하겠다구!"))
        this.setFetchStateStop()
        _connectedState.postValue(ConnectedState.DISCONNECTED)
    }

    fun hideServerCoroutineStop() {
        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소하겠다구!"))
        this.hideSetFetchStateStop()
        _connectedState.postValue(ConnectedState.DISCONNECTED)
    }
}