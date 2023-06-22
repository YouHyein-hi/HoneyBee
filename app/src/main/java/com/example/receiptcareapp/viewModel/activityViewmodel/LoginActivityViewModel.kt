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

    init {
        Log.e("TAG", "LoginActivityViewModel 생성: ", )
    }
    //LiveData는 immutable하기에 thread-safe 하지만 값을 변경할 수 없어서 MutableLiveData를 통해 값을 변경하고
    //LiveData는 해당 값을 immutable하게 받는 것입니다.
    private var _response = MutableLiveData<String>()
    val response : LiveData<String>
        get() = _response

    fun requestLogin(email:String, password:String){
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "requestLogin: $email $password", )
            _response.postValue(retrofitUseCase.requestLoginUseCase(email = email, password = password))
            Log.e("TAG", "requestLogin: ${_response.value}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "LoginActivityViewModel 삭제: ", )
    }
}