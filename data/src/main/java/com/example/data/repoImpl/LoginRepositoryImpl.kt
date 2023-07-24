package com.example.data.repoImpl

import com.example.data.remote.dataSource.LoginDataSource
import com.example.data.remote.model.toDomainLoginResponse
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.repo.LoginRepository
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
): LoginRepository {
    override suspend fun requestLogin(email: String, password: String): DomainServerReponse {
        return loginDataSource.requestLogin(email = email, password = password).toDomainLoginResponse()
    }
}