package com.example.receiptcareapp.base

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.*
import com.example.receiptcareapp.state.FetchState
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 2023-02-15
 * pureum
 */
abstract class BaseViewModel(val name: String) : ViewModel(){

    protected val isLoading = MutableLiveData(false)

    private var _fetchState = MutableLiveData<Pair<Throwable, FetchState>>()
    val fetchState : LiveData<Pair<Throwable, FetchState>> get() = _fetchState
    fun initFetchState(){ _fetchState = MutableLiveData<Pair<Throwable, FetchState>>() }

    protected val waitTime = 4000L

    private val job = SupervisorJob()

    protected val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        isLoading.postValue(false)
        coroutineContext.job.cancel()
        throwable.printStackTrace()
        when(throwable){
            is SocketException -> _fetchState.value = Pair(throwable, FetchState.BAD_INTERNET)
            is HttpException -> _fetchState.value = Pair(throwable, FetchState.PARSE_ERROR)
            is UnknownHostException -> _fetchState.value = Pair(throwable, FetchState.WRONG_CONNECTION)
            is SQLiteConstraintException -> _fetchState.value = Pair(throwable, FetchState.SQLITE_CONSTRAINT_PRIMARY_KEY)
            is SocketTimeoutException -> _fetchState.value = Pair(throwable, FetchState.SOCKET_TIMEOUT_EXCEPTION)
            is IllegalStateException -> _fetchState.value = Pair(throwable, FetchState.ILLEGAL_STATE_EXCEPTION)
            else -> _fetchState.value = Pair(throwable, FetchState.FAIL)
        }
    }

    protected val modelScope = viewModelScope + job + exceptionHandler
    protected val ioScope = CoroutineScope(Dispatchers.IO) + job + exceptionHandler
}