package com.example.receiptcareapp.base

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
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
    init {
        Log.e("TAG", "$name 생성", )
    }
    //로딩 관리
    protected val isLoading = MutableLiveData(false)

    private var _fetchState = MutableLiveData<Pair<Throwable, FetchState>>()
    val fetchState : LiveData<Pair<Throwable, FetchState>> get() = _fetchState
    fun initFetchState(){ _fetchState = MutableLiveData<Pair<Throwable, FetchState>>() }

    protected val waitTime = 4000L

    private val job = SupervisorJob()

    protected val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        isLoading.postValue(false)
        coroutineContext.job.cancel() // 여기에 coroutineContext를 붙여야 정상 취소되는데 왜그럴까?
        throwable.printStackTrace()
        when(throwable){
            is SocketException -> _fetchState.value = Pair(throwable, FetchState.BAD_INTERNET)
            is HttpException -> _fetchState.value = Pair(throwable, FetchState.PARSE_ERROR)
            is UnknownHostException -> _fetchState.value = Pair(throwable, FetchState.WRONG_CONNECTION)
            is SQLiteConstraintException -> _fetchState.value = Pair(throwable, FetchState.SQLITE_CONSTRAINT_PRIMARYKEY)
            is SocketTimeoutException -> _fetchState.value = Pair(throwable, FetchState.SOCKET_TIMEOUT_EXCEPTION)
            is IllegalStateException -> _fetchState.value = Pair(throwable, FetchState.IllegalStateException)
            else -> _fetchState.value = Pair(throwable, FetchState.FAIL)
        }
        Log.e("TAG", "base err fetchState.value : ${fetchState.value}", )
    }

    //디스패쳐 메인에서 돌아감
    //IO 에서 돌아가는 Retrofit도 해당 스코프로도 잘 돌아가는데 왜그러냐면,
    // 레트로핏 자체적으로 IO에서 실행시키는 기능때문에.
    protected val modelScope = viewModelScope + job + exceptionHandler
    protected val ioScope = CoroutineScope(Dispatchers.IO) + job + exceptionHandler

    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "$name 삭제", )
    }
}