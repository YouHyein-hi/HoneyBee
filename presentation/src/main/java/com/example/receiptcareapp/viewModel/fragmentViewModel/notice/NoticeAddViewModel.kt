package com.example.receiptcareapp.viewModel.fragmentViewModel.notice

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.notice.SendNoticeAddData
import com.example.domain.usecase.notice.AddNoticeUseCase
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

/**
 * 2023-08-13
 * pureum
 */
@HiltViewModel
class NoticeAddViewModel @Inject constructor(
    private val addNoticeUseCase: AddNoticeUseCase
): BaseViewModel("NoticeAddViewModel") {

    val loading : MutableLiveData<Boolean> get() = isLoading

    private val _response = MutableLiveData<ServerResponseData?>()
    val response : MutableLiveData<ServerResponseData?> get() = _response

    fun insertNotice(data: SendNoticeAddData){
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                isLoading.postValue(true)
                _response.postValue(addNoticeUseCase(data))
                isLoading.postValue(false)
            }
        }
    }
}