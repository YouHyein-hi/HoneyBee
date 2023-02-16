package com.example.receiptcareapp.viewModel

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
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val retrofitUseCase : RetrofitUseCase,
    private val roomUseCase : RoomUseCase
): BaseViewModel() {

    private val _sendResult = MutableLiveData<DomainSendData>()
    val sendResult : LiveData<DomainSendData>
        get() = _sendResult

    private val _receiveResult = MutableLiveData<DomainReceiveData>()
    val receiveResult : LiveData<DomainReceiveData>
        get() = _receiveResult

    private var _getRoomData = MutableLiveData<ArrayList<DomainRoomData>>()
    val getRoomData : LiveData<ArrayList<DomainRoomData>>
        get() = _getRoomData


    fun sendData(card:String, date:String, picture:ByteArray){
        viewModelScope.launch(exceptionHandler) {
            val result = retrofitUseCase.sendDataUseCase(card, date, picture)
            Log.e("TAG", "sendData: $result ")
            _sendResult.value = result
        }
    }

    fun receiveData(){
        viewModelScope.launch(exceptionHandler) {
            val result = retrofitUseCase.receiveDataUseCase()
            Log.e("TAG", "sendData: $result ")
            _receiveResult.value = result
        }
    }

    fun insertData(list: DomainRoomData){
        viewModelScope.launch(exceptionHandler) {
            roomUseCase.insertData(list)
        }
    }

    fun getAllData(){
        viewModelScope.launch(exceptionHandler) {
            _getRoomData.postValue(roomUseCase.getAllData())
        }
    }

    fun deleteData(date:String){
        viewModelScope.launch(exceptionHandler){
            roomUseCase.deleteData(date)
        }
    }
}