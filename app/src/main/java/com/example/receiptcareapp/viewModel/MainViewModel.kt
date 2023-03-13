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
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Url
import java.io.File
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
    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

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

            val result = retrofitUseCase.sendDataUseCase(card = card, amount = replacedAmount.toInt(), pictureName = "sssss", date = date, bill = uriToMultiPartBody(picture))
            Log.e("TAG", "sendData: $result ")
            _sendResult.value = result
        }
    }

    fun uriToMultiPartBody(picture: Uri): MultipartBody.Part {
        //uri를 받아서 그 사진의 절대경로를 얻어온 후 이 경로를 사용하여 사진을 file 변수에 저장
        val file = File(absolutelyPath(picture, myCotext))
        //다음을 통해 request로 바꿔준 후
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        //다음 구문을 통해 form-data 형식으로 바꿔줌
        return MultipartBody.Part.createFormData("bill", file.name, requestFile)
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

    fun receiveData(){
        CoroutineScope(exceptionHandler).launch {
            val result = retrofitUseCase.receiveDataUseCase()
            Log.e("TAG", "sendData: $result ")
            _receiveResult.value = result
        }
    }

    fun insertData(date: LocalDateTime, amount : String, card:String, picture: Uri){
//        CoroutineScope(exceptionHandler).launch {
//            var myAmount = amount
//            if(myAmount.contains(",")) { myAmount = myAmount.replace(",", "") }
//            roomUseCase.insertData(DomainRoomData(date, myAmount.toInt(), card, uriToMultiPartBody(picture)))
//        }
    }

    fun getAllData(){
        CoroutineScope(exceptionHandler).launch {
            _getRoomData.postValue(roomUseCase.getAllData())
        }
    }

    fun deleteData(date:String){
        CoroutineScope(exceptionHandler).launch{
            roomUseCase.deleteData(date)
        }
    }

}