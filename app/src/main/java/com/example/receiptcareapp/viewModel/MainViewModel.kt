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
import com.example.receiptcareapp.dto.ServerCardData
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
import kotlin.coroutines.coroutineContext

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val retrofitUseCase: RetrofitUseCase,
    private val roomUseCase: RoomUseCase

) : BaseViewModel() {

    init {
        Log.e("TAG", "MainViewModel: start")

    }

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
        card: String,
        picture: Uri,
        pictureName: String
    ) {
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "보내는 데이터 : $date, $amount, $card, $picture, $pictureName")
            // 각 데이터를 MultiPart로 변환
            val myCard = MultipartBody.Part.createFormData("cardName", card)
            val myAmount = MultipartBody.Part.createFormData("amount", amount)
            val myPictureName = MultipartBody.Part.createFormData("pictureName", "pictureName")

            val myDate = MultipartBody.Part.createFormData("date", date.toString())

            val file = File(absolutelyPath(picture, myCotext))
            //uri를 받아서 그 사진의 절대경로를 얻어온 후 이 경로를 사용하여 사진을 file 변수에 저장
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            //다음을 통해 request로 바꿔준 후
            val myPicture = MultipartBody.Part.createFormData("bill", file.name, requestFile)
            //다음 구문을 통해 form-data 형식으로 바꿔줌

            val result = retrofitUseCase.sendDataUseCase(
                cardName = myCard,
                amount = myAmount,
                pictureName = myPictureName,
                date = myDate,
                picture = myPicture
            )
            Log.e("TAG", "sendData 응답 : $result ")

            if (result == "success") {
                _serverState.postValue(ServerState.SUCCESS)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                insertData(
                    cardName = card,
                    amount = amount,
                    pictureName = "pifTJQJctureName",
                    date = date.toString(),
                    picture = picture.toString()
                )
            } else if (result == "false") {
                Log.e("TAG", "sendData: 실패입니다!", )
                _serverState.postValue(ServerState.FALSE)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                Exception("오류! 전송 실패.")
            } else {
                Exception("서버 연결 실패.")
            }
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
        pictureName: String,
        date: String,
        picture: String
    ) {
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "insertData : $date, $cardName, $amount, $pictureName, $picture,")
            roomUseCase.insertData(
                DomainRoomData(
                    cardName = cardName,
                    amount = amount,
                    pictureName = pictureName,
                    date = date,
                    picture = picture
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

    // 카드 목록 서버에 전송하기
    fun sendCard(cardList : Map<String, Int>){
        _serverJob.value = CoroutineScope(exceptionHandler).launch{
            Log.e("TAG", "CardList : ${cardList}", )
            val myCardList = MultipartBody.Part.createFormData("cardList", cardList.toString())
            val result = retrofitUseCase.sendCardUseCae(
                cardList = myCardList
            )
            Log.e("TAG", "sendCard: ${result}", )
        }
    }
}