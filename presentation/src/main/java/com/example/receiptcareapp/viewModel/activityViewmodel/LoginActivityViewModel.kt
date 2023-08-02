package com.example.receiptcareapp.viewModel.activityViewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.usecase.login.LoginUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.dto.LoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
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
    application: Application,
//    private val retrofitUseCase: RetrofitUseCase,
    private val loginUseCase: LoginUseCase,
    private val preferenceManager: PreferenceManager

    //sharedpreference 사용해서 로그인한 핸드폰은 자동로그인
): BaseViewModel(application) {
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
            withTimeoutOrNull(waitTime) { //인터셉터
                Log.e("TAG", "requestLogin: $email $password",)
                _response.postValue(
                    loginUseCase.invoke(
                        email = email,
                        password = password
                    )
                )
                _connectedState.postValue(ConnectedState.DISCONNECTED)
            } ?: throw SocketTimeoutException()
        }
    }

    fun putLoginData(id:String, pw:String){
        preferenceManager.putLogin(id)
        preferenceManager.putPassword(pw)
    }

    fun getLoginData(): LoginData = LoginData(preferenceManager.getLogin(), preferenceManager.getPassword())


    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "LoginActivityViewModel 삭제: ", )
    }
}