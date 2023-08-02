package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.data.DeleteDataUseCase
import com.example.domain.usecase.room.DeleteDataRoomUseCase
import com.example.domain.usecase.room.GetDataListRoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.io.FileOutputStream
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 2023-06-21
 * pureum
 */
class RecyclerShowViewModel @Inject constructor(
    application: Application,
    private val deleteDataRoomUseCase: DeleteDataRoomUseCase,
    private val getRoomDataListUseCase: GetDataListRoomUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val getLocalAllBill
) : BaseViewModel(application) {

    // TODO RecyclerShowFragment에만 들어가는 코드인데 RecyclerShowViewModel
    fun deleteRoomData(date: String) {
        CoroutineScope(exceptionHandler).launch {
            val result = deleteDataRoomUseCase(date)
            Log.e("TAG", "deleteRoomData result : $result", )
            //삭제 후에 데이터 끌어오기 위한 구성
            receiveAllLocalData()
        }
    }

    fun receiveAllLocalData() {
        CoroutineScope(exceptionHandler).launch {
            val gap = getRoomDataListUseCase()
            Log.e("TAG", "receiveAllRoomData: $gap")
            _roomData.postValue(gap)
        }
    }

    // TODO RecyclerShowFragment에만 들어가는 코드인데 RecyclerShowViewModel에 옮길까
    fun deleteServerBillData(id: Long) {
        Log.e("TAG", "deleteServerData: 들어감", )
        CoroutineScope(exceptionHandler).async {
            withTimeoutOrNull(waitTime) {
                val gap = deleteDataUseCase(id)
                Log.e("TAG", "deleteServerData return: $gap")
                _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
            } ?: throw SocketTimeoutException()
        }
    }

    // TODO RecyclerShowFragment에만 들어가는 코드인데 RecyclerShowViewModel
    fun deleteRoomBillData(date: String) {
        CoroutineScope(exceptionHandler).launch {
            val result = deleteDataRoomUseCase(date)
            Log.e("TAG", "deleteRoomData result : $result", )
            //삭제 후에 데이터 끌어오기 위한 구성
            getLocalAllBillData()
        }
    }

    fun getLocalAllBillData() {
        CoroutineScope(exceptionHandler).launch {
            val gap = getRoomDataListUseCase()
            Log.e("TAG", "receiveAllRoomData: $gap")
            _roomData.postValue(gap)
        }
    }


//    fun bitmapToUri(activity: Activity, bitmap: Bitmap): Uri {
//        val file = File(activity.cacheDir, "temp_image.jpg")
//        val outputStream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        outputStream.flush()
//        outputStream.close()
//        return Uri.fromFile(file)
//    }

//    fun uriToBitmap(activity:Activity, uri:Uri):Bitmap{
//        return ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.contentResolver,uri))
//    }
}