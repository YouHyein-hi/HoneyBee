package com.example.receiptcareapp.viewModel.fragmentViewModel.record

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.BillResponseData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.usecase.bill.GetDataListUseCase
import com.example.domain.usecase.bill.GetPictureDataUseCase
import com.example.domain.usecase.room.GetDataListRoomUseCase
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    val loading : MutableLiveData<Boolean> get() = isLoading

    //서버에서 받은 데이터 담는 박스
    private val _billList = MutableLiveData<BillResponseData>()
    val billList: LiveData<BillResponseData> get() = _billList

    //룸에서 받은 데이터 담는 박스
    private var _roomData = MutableLiveData<MutableList<DomainRoomData>>()
    val roomData: LiveData<MutableList<DomainRoomData>> get() = _roomData

    fun getLocalAllData() {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
        modelScope.launch {
            withTimeoutOrNull(waitTime){
                loading.postValue(true)
                Log.e("TAG", "receiveServerAllData: ", )
                val gap = getDataListUseCase()
                Log.e("TAG", "receiveServerAllData: $gap", )
                _billList.postValue(gap)
                loading.postValue(false)
            } ?: throw SocketTimeoutException()
        }
    }
}