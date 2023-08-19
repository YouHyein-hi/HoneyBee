package com.example.domain.repo

import com.example.domain.model.receive.DomainServerResponse

/**
 * 2023-07-23
 * pureum
 */
interface LoginRepository {
    suspend fun requestLogin(email:String, password:String): DomainServerResponse
}