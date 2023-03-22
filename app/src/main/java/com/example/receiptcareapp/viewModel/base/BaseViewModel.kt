package com.example.receiptcareapp.viewModel.base

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 2023-02-15
 * pureum
 */
abstract class BaseViewModel  : ViewModel(){

    private val _fetchState = MutableLiveData<Pair<Throwable, FetchState>>()
    val fetchState : LiveData<Pair<Throwable, FetchState>>
        get() = _fetchState


    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("TAG", "오잉?: ", )
        throwable.printStackTrace()
        when(throwable){
            is SocketException -> _fetchState.postValue(Pair(throwable, FetchState.BAD_INTERNET))
            is HttpException -> _fetchState.postValue(Pair(throwable, FetchState.PARSE_ERROR))
            is UnknownHostException -> _fetchState.postValue(Pair(throwable, FetchState.WRONG_CONNECTION))
            is SQLiteConstraintException -> _fetchState.postValue(Pair(throwable,FetchState.SQLITE_CONSTRAINT_PRIMARYKEY))
            is SocketTimeoutException -> _fetchState.postValue(Pair(throwable,FetchState.SOCKET_TIMEOUT_EXCEPTION))
            else -> _fetchState.postValue(Pair(throwable, FetchState.FAIL))
        }
    }
}