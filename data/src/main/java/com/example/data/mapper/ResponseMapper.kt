package com.example.data.mapper

import ServerResponse
import com.example.data.remote.model.*
import com.example.domain.model.remote.receive.basic.ServerResponseData
import com.example.domain.model.remote.receive.basic.ServerUidData
import com.example.domain.model.remote.receive.bill.ServerBillData
import com.example.domain.model.remote.receive.bill.ServerDetailBillData
import com.example.domain.model.remote.receive.bill.ServerStoreData
import com.example.domain.model.remote.receive.card.ServerCardData
import com.example.domain.model.remote.receive.card.ServerCardDetailData
import com.example.domain.model.remote.receive.card.ServerCardSpinnerData
import com.example.domain.model.remote.receive.notice.ServerNoticeData


object ResponseMapper {

    fun <T: List<ServerCardResponse>> ServerResponse<T>.toServerCardData() = ServerCardData(status, message, body?.map { it.toCardData() })

    fun ServerResponse<String>.toServerResponseData() = ServerResponseData(status, message, body)

    fun <T: List<ServerCardResponse>> ServerResponse<T>.toServerCardSpinnerData() = ServerCardSpinnerData(status, message, body?.map { it.toCardSpinnerData() })

    fun ServerResponse<ServerCardDetailResponse>.toServerCardDetailData() = ServerCardDetailData(status, message, body?.toServerCardDetailData())

    fun ServerResponse<List<ServerBillResponse>>.toServerBillData(): ServerBillData = ServerBillData(status, message, body?.map { it.toBillData() } )

    fun ServerResponse<ServerDetailBillResponse>.toServerDetailBillData(): ServerDetailBillData = ServerDetailBillData(status, message, body?.toDetailBillData())

    fun ServerResponse<List<ServerNoticeResponse>>.toServerNoticeData() = ServerNoticeData(status, message, body?.map { it.toNoticeData() })

    fun ServerResponse<Int>.toUidServerResponseData() = ServerUidData(status, message, body)

    fun ServerResponse<List<String>>.toServerStoreData() = ServerStoreData(status, message, body)

}