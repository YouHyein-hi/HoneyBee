package com.example.receiptcareapp.viewModel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DomainReceiveData
import com.example.domain.model.DomainRoomData
import com.example.domain.model.DomainSendData
import com.example.domain.usecase.RetrofitUseCase
import com.example.domain.usecase.RoomUseCase
import com.example.receiptcareapp.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val retrofitUseCase : RetrofitUseCase,
    private val roomUseCase: RoomUseCase

): BaseViewModel() {

    //이렇게 쓰면 메모리 누수가 일어난다는데 왜??
    var myCotext:Context? = null

    private val _sendResult = MutableLiveData<DomainSendData>()
    val sendResult : LiveData<DomainSendData>
        get() = _sendResult

    private val _receiveResult = MutableLiveData<DomainReceiveData>()
    val receiveResult : LiveData<DomainReceiveData>
        get() = _receiveResult

    private var _getRoomData = MutableLiveData<ArrayList<DomainRoomData>>()
    val getRoomData : LiveData<ArrayList<DomainRoomData>>
        get() = _getRoomData


    fun sendData(date: LocalDateTime, amount : String, card:String, picture: Uri){
        viewModelScope.launch(exceptionHandler) {
            Log.e("TAG", "보내는 데이터 : $date, $amount, $card, $picture", )

            var replacedAmount = amount
            if(replacedAmount.contains(",")) { replacedAmount = replacedAmount.replace(",", "") }

            // 각 데이터를 MultiPart로 변환
            val myCard = MultipartBody.Part.createFormData("cardName",card)
            val myAmount = MultipartBody.Part.createFormData("amount",replacedAmount)
            val myPictureName = MultipartBody.Part.createFormData("pictureName","pictureName")
            val myDate = MultipartBody.Part.createFormData("date",date.toString())

            // 사진을 MultiPart로 변환
            val file = File(absolutelyPath(picture, myCotext)) //uri를 받아서 그 사진의 절대경로를 얻어온 후 이 경로를 사용하여 사진을 file 변수에 저장
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull()) //다음을 통해 request로 바꿔준 후
            val myPicture = MultipartBody.Part.createFormData("bill", file.name, requestFile) //다음 구문을 통해 form-data 형식으로 바꿔줌

            try {
                val result = retrofitUseCase.sendDataUseCase(cardName = myCard, amount = myAmount, pictureName = myPictureName, date = myDate, picture = myPicture)
            }catch (E:Exception){
                Log.e("TAG", "sendData err : $E", )
            }

            //정상적으로 보내졌다면 DB에 보내기
            //if(result.response.toString() == "success") insertData(cardName = card, amount = replacedAmount, pictureName = "pictureName", date = date, picture = picture)

            //Log.e("TAG", "sendData 응답 : $result ")
            //_sendResult.value = result
            Log.e("TAG", "_sendResult: $_sendResult", )
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

    fun insertData(cardName : String, amount : String, pictureName: String, date: LocalDateTime, picture: Uri){
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "insertData : $date, $cardName, $amount, $pictureName, $picture,", )
            roomUseCase.insertData(DomainRoomData(cardName = cardName, amount = amount, pictureName = pictureName, date = date, picture = picture))
        }
    }

    fun receiveData(){
        CoroutineScope(exceptionHandler).launch {
            val result = retrofitUseCase.receiveDataUseCase()
            Log.e("TAG", "sendData: $result ")
            _receiveResult.value = result
        }
    }



    fun getAllData(){
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "getAllData: start", )
            _getRoomData.postValue(roomUseCase.getAllData())
        }
    }

    fun deleteData(date:String){
        CoroutineScope(exceptionHandler).launch{
            Log.e("TAG", "deleteData: start", )
            roomUseCase.deleteData(date)
        }
    }

}