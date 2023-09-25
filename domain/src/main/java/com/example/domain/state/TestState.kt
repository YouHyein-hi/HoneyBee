package com.example.domain.state

/**
 * 2023-09-20
 * pureum
 */
sealed class TestState<T>{
    data class Success<T>(val data: T): TestState<T>()
    data class Error<T>(val data:T? = null, val failure: String): TestState<T>()
}

