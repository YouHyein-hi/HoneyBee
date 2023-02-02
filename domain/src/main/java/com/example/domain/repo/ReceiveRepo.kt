package com.example.domain.repo

import com.example.domain.model.DomainReceiveData

/**
 * 2023-02-02
 * pureum
 */
interface ReceiveRepo {
    suspend fun receiveDataRepo():DomainReceiveData
}