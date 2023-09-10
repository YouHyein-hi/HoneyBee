package com.example.domain.repo

import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.receive.bill.ServerBillData
import com.example.domain.model.remote.receive.bill.ServerPictureData
import com.example.domain.model.remote.receive.bill.ServerStoreData
import com.example.domain.model.remote.send.bill.SendBillUpdateData
import com.example.domain.model.ui.bill.UiBillData

/**
 * 2023-07-23
 * pureum
 */
interface GeneralRepository {
    suspend fun deleteDataRepository(id: Long): ServerUidData
    suspend fun getDataListRepository(): ServerBillData
    suspend fun getStoreListRepository(): ServerStoreData
    suspend fun getPictureDataRepository(uid:String): ServerPictureData
    suspend fun insertDataRepository(sendBillData: UiBillData): ServerUidData
    suspend fun updateDataRepository(sendBillUpdateData: SendBillUpdateData): ServerUidData
}