package com.example.receiptcareapp.viewModel.activityViewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.usecase.login.LoginUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.dto.LoginData
import com.example.receiptcareapp.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val loginUseCase: LoginUseCase,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    //서버 연결 유무 관리
    private var _connectedState = MutableLiveData<ConnectedState>()
    val connectedState: LiveData<ConnectedState> get() = _connectedState
    fun changeState(state: ConnectedState){
        _connectedState.value = state
    }

    val loading: LiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response : LiveData<ResponseState> get() = _response

    fun requestLogin(email:String, password:String){
        modelScope.launch {
            withTimeoutOrNull(waitTime){
                isLoading.postValue(true)
                updateLoginState(
                    loginUseCase.invoke(
                        email = email,
                        password = password
                    ),
                    email,
                    password
                )
                isLoading.postValue(false)
            } ?: SocketTimeoutException()
        }
    }

    //여기는 어차피 항상 성공할때만 들어오는것같음
    //이걸 지워야하나
    private fun updateLoginState(response: DomainServerReponse,email: String, password: String){
        when(response.status){
            "200"->{
                putLoginData(email, password)
                _response.postValue(ResponseState.SUCCESS)
            }
            else -> _response.postValue(ResponseState.FALSE)
        }
    }

    private fun putLoginData(id:String, pw:String){
        preferenceManager.putLogin(id)
        preferenceManager.putPassword(pw)
    }

    fun getLoginData(): LoginData = LoginData(preferenceManager.getLogin(), preferenceManager.getPassword())


    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "LoginActivityViewModel 삭제: ", )
    }

    init { Log.e("TAG", "LoginActivityViewModel 생성: ", ) }
}