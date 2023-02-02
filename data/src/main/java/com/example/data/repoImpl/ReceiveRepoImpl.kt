package com.example.data.repoImpl

import com.example.data.remote.dataSource.ReceiveSource
import com.example.data.remote.dto.toDomainReceiveData
import com.example.domain.model.DomainReceiveData
import com.example.domain.repo.ReceiveRepo
import javax.inject.Inject

/**
 * 2023-02-02
 * pureum
 */
class ReceiveRepoImpl @Inject constructor(
    private val receiveSource: ReceiveSource
):ReceiveRepo{
    override suspend fun receiveDataRepo(): DomainReceiveData {
        return receiveSource.receiveDataSource().toDomainReceiveData()
    }

}