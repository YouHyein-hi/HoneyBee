package com.example.data.repoImpl

import com.example.data.remote.dataSource.LoginDataSource
import com.example.domain.repo.LoginRepository
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {
    //    override suspend fun requestLogin(email: String, password: String): DomainServerResponse {
//        return loginDataSource.requestLogin(email = email, password = password).toDomainServerResponse()
//    }
    override suspend fun requestLogin(email: String, password: String) {
        loginDataSource.requestLogin(email = email, password = password)
    }
//        return loginDataSource.requestLogin(email = email, password = password).toDomainServerResponse()
//    }
}