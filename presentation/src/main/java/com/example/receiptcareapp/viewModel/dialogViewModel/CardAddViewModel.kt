package com.example.receiptcareapp.viewModel.dialogViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.receiptcareapp.state.ResponseState
import javax.inject.Inject

@HiltViewModel
class CardAddViewModel @Inject constructor() : BaseViewModel("CardAddViewModel") {

    val loading : MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response: LiveData<ResponseState> get() = _response
}