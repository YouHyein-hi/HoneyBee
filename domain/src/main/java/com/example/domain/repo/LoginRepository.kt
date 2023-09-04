package com.example.domain.repo

import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.login.SendLoginData

/**
 * 2023-07-23
 * pureum
 */
interface LoginRepository {
    suspend fun requestLogin(loginData: SendLoginData): ServerResponseData
}