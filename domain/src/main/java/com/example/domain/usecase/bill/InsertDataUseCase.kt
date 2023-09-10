package com.example.domain.usecase.bill

import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.send.bill.SendBillData
import com.example.domain.model.ui.bill.UiBillData
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class InsertDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(sendBillData: UiBillData): ServerUidData {
        return generalRepository.insertDataRepository(sendBillData)
    }
}