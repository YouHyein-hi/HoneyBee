package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.data.GetDataListUseCase
import com.example.domain.usecase.data.GetPictureDataUseCase
import com.example.domain.usecase.room.GetDataListRoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseViewModel
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
class RecyclerViewModel @Inject constructor(
    application: Application,
    private val getRoomDataListUseCase: GetDataListRoomUseCase,
    private val getDataListUseCase: GetDataListUseCase,
    private val getPictureDataUseCase: GetPictureDataUseCase,

): BaseViewModel(application) {

    private var _startGap = MutableLiveData<ShowType>()
    val startGap : LiveData<ShowType>
        get() = _startGap
    fun changeStartGap(type: ShowType){ _startGap.value = type }

    fun receiveAllLocalData() {
        CoroutineScope(exceptionHandler).launch {
            val gap = getRoomDataListUseCase()
            Log.e("TAG", "receiveAllRoomData: $gap")
            _roomData.postValue(gap)
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
                val gap = getDataListUseCase()
                Log.e("TAG", "receiveServerAllData: $gap", )
                _serverData.postValue(gap)
            } ?: throw SocketTimeoutException()
        }
    }

    // TODO RecyclerFragment에만 들어가는 코드인데 RecyclerViewModel에 옮길까
    fun getServerPictureData(uid:String){
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = getPictureDataUseCase(uid)
                Log.e("TAG", "receiveServerPictureData gap : $gap", )
                _picture.postValue(gap)
            }?:throw SocketTimeoutException()
        }
    }

}