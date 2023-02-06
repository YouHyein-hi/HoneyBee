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

    private val _sendResult = MutableLiveData<List<DomainSendData>>()
    val sendResult : LiveData<List<DomainSendData>>
        get() = _sendResult

    fun sendData(){
        viewModelScope.launch {
            var data = sendUseCase.invoke()
            Log.e("TAG", "sendData: ${data} ")
            _sendResult.value = data
        }
    }
}