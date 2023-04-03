package com.example.receiptcareapp.viewModel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.DomainReceiveAllData
import com.example.domain.model.DomainReceiveCardData
import com.example.domain.model.DomainRoomData
import com.example.domain.usecase.RetrofitUseCase
import com.example.domain.usecase.RoomUseCase
import com.example.receiptcareapp.State.ConnetedState
import com.example.receiptcareapp.State.ServerState
import com.example.receiptcareapp.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InterruptedIOException
import java.time.LocalDateTime
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
    private var _roomData = MutableLiveData<ArrayList<DomainRoomData>>()
    val roomData: LiveData<ArrayList<DomainRoomData>>
        get() = _roomData

    //서버 연결 유무 관리
    private var _connectedState = MutableLiveData<ConnetedState>()
    val connectedState: LiveData<ConnetedState>
        get() = _connectedState
    fun changeConnectedState(connetedState: ConnetedState) {
        Log.e("TAG", "changeConnectedState: $connetedState", )
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
    val serverJob: LiveData<Job>
        get() = _serverJob

    //서버에 데이터 전송 기능
    fun sendData(
        date: LocalDateTime,
        amount: String,
        cardName: String,
        file: Uri,
        storeName: String
    ) {
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "보내는 데이터 : $date, $amount, $cardName, $file, $storeName")
            val myCard = MultipartBody.Part.createFormData("cardName", cardName)
            val myAmount = MultipartBody.Part.createFormData("amount", amount)
            val myStoreName = MultipartBody.Part.createFormData("storeName", storeName)
            val myDate = MultipartBody.Part.createFormData("date", date.toString())

            //file은 리퀘스트 바디로 바꾼 후 multipartbody로 바꿔야 함.
            val file = File(absolutelyPath(file, myCotext))
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val myPicture = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val result = retrofitUseCase.sendDataUseCase(
                cardName = myCard,
                amount = myAmount,
                pictureName = myStoreName,
                date = myDate,
                picture = myPicture
            )

            Log.e("TAG", "sendData 응답 : $result ")
            if (result == "add success") {
                _serverState.postValue(ServerState.SUCCESS)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                insertData(
                    cardName = cardName,
                    amount = amount,
                    storeName = storeName,
                    date = date.toString(),
                    file = file.toString()
                )
            } else {
                Log.e("TAG", "sendData: 실패입니다!", )
                _serverState.postValue(ServerState.FALSE)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                Exception("오류! 전송 실패.")
            }
            /*else {
                Exception("서버 연결 실패.")
            }*/
        }
    }

    fun sendCardData(cardName: String, amount: String) {
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch{
            val myCardName = MultipartBody.Part.createFormData("cardName", cardName)
            val myAount = MultipartBody.Part.createFormData("cardName", amount)
            retrofitUseCase.sendCardDataUseCase(cardName = myCardName, amount = myAount)

            // 성공하면 값을 불러오기
            receiveCardData()
        }
    }

    fun receiveCardData(){
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value =CoroutineScope(exceptionHandler).launch{
            _cardData.value = retrofitUseCase.receiveCardDataUseCase()
            // 통신 끝나면 커넥트 풀어주기
            _connectedState.value = ConnetedState.DISCONNECTED
            // 라이브데이터로 관리하기!
            // 결과값을 분기문으로 관리 + 커넥트 풀어주기
        }
    }

    fun deleteCardData(){
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch{
            retrofitUseCase.deleteCardDataUseCase()
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

    fun insertData(
        cardName: String,
        amount: String,
        storeName: String,
        date: String,
        file: String
    ) {
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "insertData : $date, $cardName, $amount, $storeName, $file,")
            roomUseCase.insertData(
                DomainRoomData(
                    cardName = cardName,
                    amount = amount,
                    storeName = storeName,
                    date = date,
                    file = file
                )
            )
            _connectedState.postValue(ConnetedState.CONNECTING)
        }
    }

    fun getAllLocalData() {
        //_connectedState.value = ConnetedState.CONNECTING
        CoroutineScope(exceptionHandler).launch {
            val gap = roomUseCase.getAllData()
            _roomData.postValue(gap)
            Log.e("TAG", "getAllLocalData: $gap")
            //_connectedState.value = ConnetedState.DISCONNECTED
        }
    }

    fun deleteRoomData(date: String) {
        CoroutineScope(exceptionHandler).launch {
            roomUseCase.deleteData(date)
            //삭제 후에 데이터 끌어오기 위한 구성
            getAllLocalData()
        }
    }

    fun getAllServerData() {
        Log.e("TAG", "getAllServerData: ", )
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            _serverData.postValue(retrofitUseCase.receiveDataUseCase())
            _connectedState.value = ConnetedState.DISCONNECTED
        }
    }

    fun deleteServerData(data: String) {
        CoroutineScope(exceptionHandler).launch {
            val gap = retrofitUseCase.deleteServerData(data)
            Log.e("TAG", "deleteServerData return: $gap")
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

//    // 카드 목록 서버에 전송하기
//    fun sendCard(cardList : Map<String, Int>){
//        _serverJob.value = CoroutineScope(exceptionHandler).launch{
//            Log.e("TAG", "CardList : ${cardList}", )
//            val myCardList = MultipartBody.Part.createFormData("cardList", cardList.toString())
//            val result = retrofitUseCase.send(
//                cardList = myCardList
//            )
//            Log.e("TAG", "sendCard: ${result}", )
//        }
//    }
}