package com.example.domain.usecase.bill

import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.send.bill.SendBillData
import com.example.domain.model.remote.send.bill.SendBillUpdateData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class UpdateDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(sendBillUpdateData: SendBillUpdateData): ServerUidData {
        return generalRepository.updateDataRepository(sendBillUpdateData)
    }
}