package com.example.receiptcareapp.viewModel.activityViewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.usecase.RetrofitUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 2023-06-21
 * pureum
 */
@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val retrofitUseCase: RetrofitUseCase,
    //sharedpreference 사용해서 로그인한 핸드폰은 자동로그인
): BaseViewModel() {
    private var waitTime = 3000L

    init {
        Log.e("TAG", "LoginActivityViewModel 생성: ", )
    }

    //서버 연결 유무 관리
    private var _connectedState = MutableLiveData<ConnectedState>()
    val connectedState: LiveData<ConnectedState>
        get() = _connectedState
    fun changeState(state: ConnectedState){
        _connectedState.value = state
    }

    private var _response = MutableLiveData<DomainServerReponse>()
    val response : LiveData<DomainServerReponse>
        get() = _response

    fun requestLogin(email:String, password:String){
        CoroutineScope(exceptionHandler).launch {
            _connectedState.postValue(ConnectedState.CONNECTING)
            withTimeoutOrNull(waitTime) {
                Log.e("TAG", "requestLogin: $email $password",)
                _response.postValue(
                    retrofitUseCase.requestLoginUseCase(
                        email = email,
                        password = password
                    )
                )
                _connectedState.postValue(ConnectedState.DISCONNECTED)
            } ?: throw SocketTimeoutException()
        }
    }



    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "LoginActivityViewModel 삭제: ", )
    }
}