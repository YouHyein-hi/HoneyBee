package com.example.receiptcareapp.viewModel.dialogViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.state.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardDeleteViewModel @Inject constructor() : BaseViewModel("CardDeleteViewModel") {

    val loading : MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response: LiveData<ResponseState> get() = _response
}