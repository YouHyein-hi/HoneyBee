package com.example.receiptcareapp.viewModel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.*
import com.example.domain.usecase.RetrofitUseCase
import com.example.domain.usecase.RoomUseCase
import com.example.receiptcareapp.State.ConnetedState
import com.example.receiptcareapp.State.ServerState
import com.example.receiptcareapp.dto.SendCardData
import com.example.receiptcareapp.dto.SendData
import com.example.receiptcareapp.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InterruptedIOException
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
    private var _connectedState = MutableLiveData<ConnetedState>()
    val connectedState: LiveData<ConnetedState>
        get() = _connectedState

    fun changeConnectedState(connetedState: ConnetedState) {
        Log.e("TAG", "changeConnectedState: $connetedState")
        _connectedState.value = connetedState
    }

    //서버 결과값 관리
    private var _serverState = MutableLiveData<ServerState>()
    val serverState: LiveData<ServerState>
        get() = _serverState

    fun changeServerState(serverState: ServerState) {
        _serverState.value = serverState
    }

    // 서버 카드 전달받은 값 관리
    private var _cardData = MutableLiveData<MutableList<DomainReceiveCardData>>()
    val cardData: LiveData<MutableList<DomainReceiveCardData>>
        get() = _cardData


    // 코루틴 값을 담아두고 원할때 취소하기
    private var _serverJob = MutableLiveData<Job>()

    //서버에 데이터 전송 기능
    fun sendData(sendData: SendData) {

        _connectedState.value = ConnetedState.CONNECTING

        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            val myCard = MultipartBody.Part.createFormData("cardName", sendData.cardName)
            val myAmount = MultipartBody.Part.createFormData("amount", sendData.amount)
            val myStoreName = MultipartBody.Part.createFormData("storeName", sendData.storeName)
            val myDate = MultipartBody.Part.createFormData("date", sendData.date)

            //file은 리퀘스트 바디로 바꾼 후 multipartbody로 바꿔야 함.
            val file = File(absolutelyPath(sendData.picture, myCotext))
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val myPicture = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val result = retrofitUseCase.sendDataUseCase(
                DomainSendData(
                    cardName = myCard,
                    amount = myAmount,
                    storeName = myStoreName,
                    date = myDate,
                    picture = myPicture
                )
            )

            Log.e("TAG", "sendData 응답 : $result ")
            if (result == "add success") {
                _serverState.postValue(ServerState.SUCCESS)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
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
                _serverState.postValue(ServerState.FALSE)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                Exception("오류! 전송 실패.")
            }
            /*else {
                Exception("서버 연결 실패.")
            }*/
        }
    }


    fun sendCardData(sendData: SendCardData) {
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            val myCardName = MultipartBody.Part.createFormData("cardName", sendData.cardName)
            val myAount = MultipartBody.Part.createFormData("cardName", sendData.cardAmount.toString())
            retrofitUseCase.sendCardDataUseCase(
                DomainSendCardData(
                    cardName = myCardName,
                    cardAmount = myAount
                )
            )
            // 성공하면 값을 불러오기
            receiveCardData()
        }
    }

    fun receiveCardData() {
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            _cardData.value = retrofitUseCase.receiveCardDataUseCase()
            // 통신 끝나면 커넥트 풀어주기
            _connectedState.value = ConnetedState.DISCONNECTED
            // 라이브데이터로 관리하기!
            // 결과값을 분기문으로 관리 + 커넥트 풀어주기
        }
    }

    fun deleteCardData(id: Long) {
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            retrofitUseCase.deleteCardDataUseCase(id)
            // 결과값을 분기문으로 관리 + 커넥트 풀어주기

            // 성공하면 값을 불러오기
            receiveCardData()
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

    fun getAllServerData() {
        Log.e("TAG", "getAllServerData: ")
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            _serverData.postValue(retrofitUseCase.receiveDataUseCase())
            _connectedState.value = ConnetedState.DISCONNECTED
        }
    }

    fun deleteServerData(id: Long) {
        CoroutineScope(exceptionHandler).launch {
            val gap = retrofitUseCase.deleteServerData(id)
            Log.e("TAG", "deleteServerData return: $gap")
        }
    }

    fun changeServerData(sendData: SendData) {
        CoroutineScope(exceptionHandler).launch {
            retrofitUseCase.resendDataUseCase(DomainSendData(sendData.))
        }
    }

    fun changeCardData(id: Long) {
        CoroutineScope(exceptionHandler).launch {
            retrofitUseCase.resendCardDataUseCase()
        }
    }


    fun deleteRoomData(id: Long) {
        CoroutineScope(exceptionHandler).launch {
            roomUseCase.deleteData(id)
            //삭제 후에 데이터 끌어오기 위한 구성
            getAllRoomData()
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
            _connectedState.postValue(ConnetedState.CONNECTING)
        }
    }

    fun getAllRoomData() {
        CoroutineScope(exceptionHandler).launch {
            val gap = roomUseCase.getAllData()
            _roomData.postValue(gap)
            Log.e("TAG", "getAllLocalData: $gap")
        }
    }


    fun serverCoroutineStop() {
        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소하겠다구!"))
        this.setFetchStateStop()
        _connectedState.postValue(ConnetedState.DISCONNECTED)
    }

    fun hideServerCoroutineStop() {
        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소하겠다구!"))
        this.hideSetFetchStateStop()
        _connectedState.postValue(ConnetedState.DISCONNECTED)
    }
}