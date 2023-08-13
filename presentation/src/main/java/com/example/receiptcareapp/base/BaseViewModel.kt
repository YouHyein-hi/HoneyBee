package com.example.receiptcareapp.base

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.util.FetchState
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 2023-02-15
 * pureum
 */
abstract class BaseViewModel : ViewModel(){

    protected val isLoading = MutableLiveData(false)

    private val _fetchState = MutableLiveData<Pair<Throwable, FetchState>>()
    val fetchState : LiveData<Pair<Throwable, FetchState>>
        get() = _fetchState
    fun setFetchStateStop(){
        _fetchState.postValue(Pair(Throwable(""), FetchState.STOP))
    }
    fun hideSetFetchStateStop(){
        _fetchState.postValue(Pair(Throwable(""), FetchState.HIDE_STOP))
    }
    protected val waitTime = 8000L

    private val job = SupervisorJob()

    protected val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        isLoading.postValue(false)

        throwable.printStackTrace()
        Log.e("TAG", "base : $throwable", )
        when(throwable){
            is SocketException -> _fetchState.postValue(Pair(throwable, FetchState.BAD_INTERNET))
            is HttpException -> _fetchState.postValue(Pair(throwable, FetchState.PARSE_ERROR))
            is UnknownHostException -> _fetchState.postValue(Pair(throwable, FetchState.WRONG_CONNECTION))
            is SQLiteConstraintException -> _fetchState.postValue(Pair(throwable, FetchState.SQLITE_CONSTRAINT_PRIMARYKEY))
            is SocketTimeoutException -> _fetchState.postValue(Pair(throwable, FetchState.SOCKET_TIMEOUT_EXCEPTION))
            else -> _fetchState.postValue(Pair(throwable, FetchState.FAIL))
        }
    }

    //디스패쳐 메인에서 돌아감
    //TODO IO 에서 돌아가는 Retrofit도 해당 스코프로도 잘 돌아가는데 왜그럴까?
    protected val modelScope = viewModelScope + job + exceptionHandler


//    //TODO 이 기능을 Base ViewModel에 정의
//    fun serverCoroutineStop() {
//        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소"))
//        this.setFetchStateStop()
//    }
//
//    //TODO 이 기능을 Base ViewModel에 정의
//    fun hideServerCoroutineStop() {
//        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소"))
//    }
}