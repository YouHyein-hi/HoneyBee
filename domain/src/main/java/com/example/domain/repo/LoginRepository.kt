package com.example.domain.repo

import com.example.domain.model.receive.DomainServerResponse
import com.example.domain.model.receive.ServerResponseData

/**
 * 2023-07-23
 * pureum
 */
interface LoginRepository {
    suspend fun requestLogin(email:String, password:String): ServerResponseData
}