package com.example.data.repoImpl

import com.example.data.remote.dataSource.SendSource
import com.example.data.remote.dto.toDomainSendData
import com.example.domain.model.DomainSendData
import com.example.domain.repo.SendRepo
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class SendRepoImpl @Inject constructor(
    private val sendSource : SendSource
):SendRepo{
    override suspend fun sendDataRepo(): List<DomainSendData> {
        return sendSource.sendDataSource().toDomainSendData()
    }

}