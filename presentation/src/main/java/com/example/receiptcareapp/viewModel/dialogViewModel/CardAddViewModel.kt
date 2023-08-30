package com.example.receiptcareapp.viewModel.dialogViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.InsertCardUseCase
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.receiptcareapp.util.ResponseState
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CardAddViewModel @Inject constructor() : BaseViewModel("CardAddViewModel") {

    init {
        Log.e("TAG", "CardAddBottomViewModel")
    }

    val loading : MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response: LiveData<ResponseState> get() = _response
}