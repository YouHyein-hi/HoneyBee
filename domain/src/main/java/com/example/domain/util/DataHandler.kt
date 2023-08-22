package com.example.domain.util

import android.util.Log
import java.text.DecimalFormat

/**
 * 2023-08-20
 * pureum
 */

fun changeDate(inputDate: String?): String {
    Log.e("TAG", "changeDate: $inputDate", )
    //2023.08.21  15시 48분
    return if (inputDate?.contains("T")==true) {
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
