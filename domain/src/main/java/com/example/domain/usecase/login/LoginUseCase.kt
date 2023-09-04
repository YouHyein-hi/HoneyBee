package com.example.domain.usecase.login

import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.login.SendLoginData
import com.example.domain.repo.LoginRepository

/**
 * 2023-07-23
 * pureum
 */
class LoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(loginData: SendLoginData): ServerResponseData {
        return loginRepository.requestLogin(loginData)
    }
}