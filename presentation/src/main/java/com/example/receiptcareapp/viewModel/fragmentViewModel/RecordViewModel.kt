package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.usecase.data.GetDataListUseCase
import com.example.domain.usecase.data.GetPictureDataUseCase
import com.example.domain.usecase.room.GetDataListRoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.io.FileOutputStream
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 2023-06-22
 * pureum
 */
@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getRoomDataListUseCase: GetDataListRoomUseCase,
    private val getDataListUseCase: GetDataListUseCase,
    private val getPictureDataUseCase: GetPictureDataUseCase,
): BaseViewModel() {

    private var _startGap = MutableLiveData<ShowType>()
    val startGap : LiveData<ShowType>
        get() = _startGap
    fun changeStartGap(type: ShowType){ _startGap.value = type }

    val loading : MutableLiveData<Boolean> get() = isLoading

    //서버에서 받은 데이터 담는 박스
    private val _serverData = MutableLiveData<MutableList<DomainReceiveAllData>>()
    val serverData: LiveData<MutableList<DomainReceiveAllData>>
        get() = _serverData

    //룸에서 받은 데이터 담는 박스
    private var _roomData = MutableLiveData<MutableList<DomainRoomData>>()
    val roomData: LiveData<MutableList<DomainRoomData>>
        get() = _roomData



    fun getLocalAllData() {
        CoroutineScope(exceptionHandler).launch {
            loading.postValue(true)
            val gap = getRoomDataListUseCase()
            Log.e("TAG", "receiveAllRoomData: $gap")
            _roomData.postValue(gap)
            loading.postValue(false)
        }
    }

    fun bitmapToUri(activity: Activity, bitmap: Bitmap): Uri {
        val file = File(activity.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file)
    }

    // Server 데이터 불러오는 부분
    fun getServerAllBillData() {
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime){
                loading.postValue(true)
                Log.e("TAG", "receiveServerAllData: ", )
                val gap = getDataListUseCase()
                Log.e("TAG", "receiveServerAllData: $gap", )
                _serverData.postValue(gap)
                loading.postValue(false)
            } ?: throw SocketTimeoutException()
        }
    }

    // TODO RecyclerFragment에만 들어가는 코드인데 RecyclerViewModel에 옮길까
    fun getServerPictureData(uid:String){
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                loading.postValue(false)
                val gap = getPictureDataUseCase(uid)

                Log.e("TAG", "receiveServerPictureData gap : $gap", )
//                _picture.postValue(gap)
                loading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }

}