package com.example.receiptcareapp.state

/**
 * 2023-01-31
 * pureum
 */
enum class FetchState {
    BAD_INTERNET,PARSE_ERROR,WRONG_CONNECTION,FAIL,SQLITE_CONSTRAINT_PRIMARYKEY, SOCKET_TIMEOUT_EXCEPTION ,IllegalStateException
}