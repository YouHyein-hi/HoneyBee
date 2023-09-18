package com.example.receiptcareapp.viewModel.activityViewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.login.SendLoginData
import com.example.domain.usecase.login.LoginUseCase
import com.example.receiptcareapp.base.BaseViewModel
import com.example.domain.usecase.card.GetCardListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val preferenceManager: PreferenceManager,
    private val cardListUseCase: GetCardListUseCase
    ) : BaseViewModel("LoginActivityViewModel") {

    //서버 연결 유무 관리
    val loading: LiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ServerResponseData>()
    val response: LiveData<ServerResponseData> get() = _response

    fun requestLogin(email: String, password: String, autoLoginCheckBox:Boolean, savedIdCheckBoxState:Boolean) {
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                isLoading.postValue(true)
                delay(1000L)
                loginUseCase.invoke(SendLoginData(email, password)).let {response ->
                    _response.postValue(response)
                    if(response.status=="200"){
                        savedAutoLoginCheckBoxState(autoLoginCheckBox)
                        savedIdAndCheckBokState(savedIdCheckBoxState, email)
                    }
                }
                isLoading.postValue(false)
            } ?: SocketTimeoutException()
        }
    }

    fun checkAuthTime(): Boolean {
        return when(val authTime = preferenceManager.getAuthTime()){
            null -> false
            else -> {
                if(authTime.toLong() > (System.currentTimeMillis() / 1000))
                    true
                else{
                    authTimeEnd()
                    false
                }
            }
        }
    }

    private fun savedAutoLoginCheckBoxState(state:Boolean) = preferenceManager.putAutoLoginCheckBox(state)
    private fun savedIdAndCheckBokState(state:Boolean, id:String){
        if(state) preferenceManager.putEmail(id) else preferenceManager.putEmail("")
        preferenceManager.putAutoEmailCheckBox(state)
    }

    fun getAutoLoginCheckBoxState():Boolean = preferenceManager.getAutoLoginCheckBox()
    fun getId():String? = preferenceManager.getEmail()
    fun getIdCheckBoxState():Boolean = preferenceManager.getAutoEmailCheckBox()

    fun loadGetAuthData(){
        Log.e("TAG", "getAccessToken: ${preferenceManager.getAccessToken()}", )
        Log.e("TAG", "getRefreshToken: ${preferenceManager.getRefreshToken()}", )
        Log.e("TAG", "getUserRight: ${preferenceManager.getUserRight()}", )
        Log.e("TAG", "getAuthTime: ${preferenceManager.getAuthTime()}", )
    }

    fun authTimeEnd(){
        modelScope.launch {
            throw Exception("자동로그인 토큰 만료")
        }
    }
}