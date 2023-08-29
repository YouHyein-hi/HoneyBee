package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.receive.card.ServerCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.notice.GetNoticeListUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCardListUseCase: GetCardListUseCase,
    private val getNoticeListUseCase: GetNoticeListUseCase,
) : BaseViewModel("HomeViewModel") {

    init {
        Log.e("TAG", "HomeCardBottomSheetViewModel")
    }

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response: LiveData<ResponseState> get() = _response

    //서버 응답 일관화 이전에 사용할 박스
    private var _cardList = MutableLiveData<ServerCardData?>()
    val cardList: LiveData<ServerCardData?> get() = _cardList

    //서버 응답 일관화 이전에 사용할 박스
    private var _notice = MutableLiveData<String>()
    val notice: LiveData<String> get() = _notice

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardList.postValue(getCardListUseCase())
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun getNoticeList() {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                val gap = getNoticeListUseCase().body
                if (gap?.isEmpty() == true)
                    _notice.postValue("Honey Bee 영수증 관리 앱 사용을 환영합니다!")
                else
                    _notice.postValue(gap?.last()?.title)
            }
            isLoading.postValue(false)
        }
    }
}