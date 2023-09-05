package com.example.receiptcareapp.util

import com.example.receiptcareapp.state.FetchState

/**
 * 2023-08-19
 * pureum
 */
object FetchStateHandler {
    operator fun invoke(state: Pair<Throwable, FetchState>): String{
        return when (state.second) {
            FetchState.BAD_INTERNET -> "인터넷 연결 실패"
            FetchState.PARSE_ERROR -> "입력 오류"
            FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
            FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "이미 값이 저장되어있습니다."
            FetchState.SOCKET_TIMEOUT_EXCEPTION -> "연결 시간이 초과되었습니다."
            FetchState.IllegalStateException -> "서버 리턴값 받기 오류"
            else -> "저장 안된 오류!  ${state.first.message} "
        }
    }
}