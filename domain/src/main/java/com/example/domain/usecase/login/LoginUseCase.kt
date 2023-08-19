package com.example.domain.usecase.login

import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.repo.LoginRepository

/**
 * 2023-07-23
 * pureum
 */
class LoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email:String, password:String): DomainServerResponse {
        return loginRepository.requestLogin(email = email, password = password)
    }
}