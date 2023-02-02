package com.example.domain.repo

import com.example.domain.model.DomainSendData

/**
 * 2023-02-02
 * pureum
 */
interface SendRepo {
    suspend fun sendDataRepo(): List<DomainSendData>
}