package com.example.receiptcareapp.viewModel.fragmentViewModel.notice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.send.DomainAddNoticeData
import com.example.domain.usecase.notice.AddNoticeUseCase
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
): BaseViewModel() {

    val loading : MutableLiveData<Boolean> get() = isLoading

    fun insertNotice(data: DomainAddNoticeData){
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                isLoading.postValue(true)
                Log.e("TAG", "insertNotice: ${addNoticeUseCase(data)}", )
                isLoading.postValue(false)
            }
        }
    }

    fun responseUpdate(){

    }
}