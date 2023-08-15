package com.example.data.remote.model

import com.example.domain.model.send.DomainGetNoticeListData

/**
 * 2023-08-13
 * pureum
 */
data class GetNoticeListData(
    val billNoticeId: String,
    val billNoticeTitle: String,
    val billNoticeDate: String,
    val billNoticeContent: String,
)

fun GetNoticeListData.toDomainGetNoticeListData(): DomainGetNoticeListData =
    DomainGetNoticeListData(
        billNoticeId,
        billNoticeTitle,
        changeDate(billNoticeDate),
        billNoticeContent
)

fun changeDate(inputDate: String): String {
    return if (inputDate.contains(":")) {
        val gap = inputDate.replace("-", ".").split("T")
        val time = gap[1].split(":")
        "${gap[0]}.  ${time[0]}시 ${time[1]}분"
    } else{ inputDate }
}
