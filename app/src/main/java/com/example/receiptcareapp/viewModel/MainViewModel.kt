package com.example.receiptcareapp.viewModel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.DomainReceiveAllData
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

    init {
        Log.e("TAG", "MainViewModel: start", )

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
    fun changeConnectedState(connetedState: ConnetedState){
        _connectedState.value = connetedState
    }

    //서버 결과값 관리
    private var _serverState = MutableLiveData<ServerState>()
    val serverState: LiveData<ServerState>
        get() = _serverState
    fun changeServerState(serverState: ServerState){
        _serverState.value = serverState
    }

    private var _serverJob = MutableLiveData<Job>()
    val serverJob :LiveData<Job>
        get() = _serverJob

    //서버에 데이터 전송 기능
    fun sendData(date: LocalDateTime, amount: String, card: String, picture: Uri, pictureName: String) {
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

            if(result == "success"){
                _serverState.postValue(ServerState.SUCCESS)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                insertData(cardName = card, amount = amount, pictureName = "pifTJQJctureName", date = date.toString(), picture = picture.toString())
            }else if(result == "false"){
                _serverState.postValue(ServerState.FALSE)
                _connectedState.postValue(ConnetedState.DISCONNECTED)
                Exception("오류! 전송 실패.")
            }else{
                Exception("서버 연결 실패.")
            }
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

//    fun receiveData() {
//        CoroutineScope(exceptionHandler).launch {
//            val result = retrofitUseCase.receiveDataUseCase()
//            Log.e("TAG", "sendData: $result ")
//            _receiveResult.value = result
//        }
//    }


    fun getAllLocalData() {
        //_connectedState.value = ConnetedState.CONNECTING
        CoroutineScope(exceptionHandler).launch {
            val gap = roomUseCase.getAllData()
            _roomData.postValue(gap)
            Log.e("TAG", "getAllLocalData: $gap", )
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

    fun getAllServerData(){
        _connectedState.value = ConnetedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            _serverData.postValue(retrofitUseCase.receiveDataUseCase())
            _connectedState.value = ConnetedState.DISCONNECTED
        }
    }

    fun deleteServerData(data:String){
        CoroutineScope(exceptionHandler).launch {
            val gap = retrofitUseCase.deleteServerData(data)
            Log.e("TAG", "deleteServerData return: $gap", )
            //_receiveResult.value = gap
        }


    }

    fun serverCoroutineStop(){
        Log.e("TAG", "coroutineStop: stop ${_serverJob.value}", )
        _serverJob.value?.cancel("코루틴 취소")
        _connectedState.value = ConnetedState.DISCONNECTED

    }
}