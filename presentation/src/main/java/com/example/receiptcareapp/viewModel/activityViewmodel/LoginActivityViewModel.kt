package com.example.receiptcareapp.viewModel.activityViewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.login.SendLoginData
import com.example.domain.usecase.login.LoginUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.domain.model.ui.login.LoginData
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
) : BaseViewModel("LoginActivityViewModel") {

    //서버 연결 유무 관리
    val loading: LiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ServerResponseData>()
    val response: LiveData<ServerResponseData> get() = _response

    lateinit var loginData: Pair<String, String>

    fun requestLogin(email: String, password: String) {
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                isLoading.postValue(true)
                _response.postValue(
                    loginUseCase.invoke(
                        SendLoginData(email, password)
                    )
                )
                loginData = Pair(email, password)
                isLoading.postValue(false)
            } ?: SocketTimeoutException()
        }
    }

    fun putLoginData(data: LoginData) {
        preferenceManager.putLogin(data.id!!)
        preferenceManager.putPassword(data.pw!!)
    }

    fun getLoginData(): LoginData =
        LoginData(preferenceManager.getLogin(), preferenceManager.getPassword())
}