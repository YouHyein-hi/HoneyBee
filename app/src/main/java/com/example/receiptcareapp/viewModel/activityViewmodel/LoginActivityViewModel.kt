package com.example.receiptcareapp.viewModel.activityViewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.RetrofitUseCase
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
    private var waitTime = 5000L

    init {
        Log.e("TAG", "LoginActivityViewModel 생성: ", )
    }

    private var _response = MutableLiveData<String>()
    val response : LiveData<String>
        get() = _response

    fun requestLogin(email:String, password:String){
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                Log.e("TAG", "requestLogin: $email $password",)
                _response.postValue(
                    retrofitUseCase.requestLoginUseCase(
                        email = email,
                        password = password
                    )
                )
                Log.e("TAG", "requestLogin: ${_response.value}")
            } ?: throw SocketTimeoutException()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "LoginActivityViewModel 삭제: ", )
    }
}