package com.example.receiptcareapp.viewModel.fragmentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.notice.GetNoticeListUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.util.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCardListUseCase: GetCardListUseCase,
    private val getNoticeListUseCase: GetNoticeListUseCase,
    private val preferenceManager: PreferenceManager
) : BaseViewModel("HomeViewModel") {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response: LiveData<ResponseState> get() = _response

    private var _cardList = MutableLiveData<ServerCardData?>()
    val cardList: LiveData<ServerCardData?> get() = _cardList

    private var _notice = MutableLiveData<String>()
    val notice: LiveData<String> get() = _notice

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

    fun settingUserRight() { MyApplication.right = preferenceManager.getUserRight() ?: "" }
}