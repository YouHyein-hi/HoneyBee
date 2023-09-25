package com.example.data.repoImpl

import com.example.data.mapper.ResponseMapper.toServerResponseData
import com.example.data.remote.dataSource.LoginDataSource
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.send.login.SendLoginData
import com.example.domain.repo.LoginRepository
import javax.inject.Inject

/**
 * 2023-07-23
 * pureum
 */
class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {
    override suspend fun requestLogin(loginData: SendLoginData): ServerResponseData =
        loginDataSource.requestLogin(email = loginData.email, password = loginData.password)!!
            .body()!!.toServerResponseData()
}