package com.example.receiptcareapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DomainSendData
import com.example.domain.usecase.SendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val sendUseCase : SendUseCase
): ViewModel() {

    private val _sendResult = MutableLiveData<DomainSendData>()
    val sendResult : LiveData<DomainSendData>
        get() = _sendResult

    fun sendData(){
        viewModelScope.launch {
            Log.e("TAG", "sendData: ${sendUseCase.invoke()} ")
//            _sendResult.value = sendUseCase.invoke()
        }
    }
}