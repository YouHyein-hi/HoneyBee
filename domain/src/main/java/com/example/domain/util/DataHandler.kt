package com.example.domain.util

import java.text.DecimalFormat

/**
 * 2023-08-20
 * pureum
 */

fun changeDate(inputDate: String?): String {
    return if (inputDate?.contains(":")!=null) {
        val gap = inputDate.replace("-", ".").split("T")
        val time = gap[1].split(":")
        "${gap[0]}  ${time[0]}시 ${time[1]}분"
    } else {
        inputDate.toString()
    }
}

fun changeAmount(amount: String): String {
    return DecimalFormat("#,###").format(amount.toInt())
}
