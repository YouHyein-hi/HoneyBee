package com.example.data.mapper

import ServerResponse
import com.example.data.remote.model.*
import com.example.domain.model.receive.ServerResponseData
import com.example.domain.model.receive.ServerUidData
import com.example.domain.model.receive.bill.ServerBillData
import com.example.domain.model.receive.bill.ServerStoreData
import com.example.domain.model.receive.card.ServerCardData
import com.example.domain.model.receive.card.ServerCardSpinnerData
import com.example.domain.model.receive.notice.ServerNoticeData

/**
 * 2023-08-30
 * pureum
 */
object ResponseMapper {

    fun <T: List<ServerCardResponse>> ServerResponse<T>.toServerCardData() = ServerCardData(status, message, body?.map { it.toCardData() })

    fun ServerResponse<String>.toServerResponseData() = ServerResponseData(status, message, body)

    fun <T: List<ServerCardResponse>> ServerResponse<T>.toServerCardSpinnerData() = ServerCardSpinnerData(status, message, body?.map { it.toCardSpinnerData() })

    fun ServerResponse<List<ServerBillResponse>>.toServerBillData(): ServerBillData = ServerBillData(status, message, body?.map { it.toBillData() } )

    fun ServerResponse<List<ServerNoticeResponse>>.toServerNoticeData() = ServerNoticeData(status, message, body?.map { it.toNoticeData() })

    fun ServerResponse<Int>.toUidServerResponseData() = ServerUidData(status, message, body)

    fun ServerResponse<List<String>>.toServerStoreData() = ServerStoreData(status, message, body)
}