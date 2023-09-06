package com.example.domain.util

import android.util.Log
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 2023-08-20
 * pureum
 */
object StringUtil {
    fun changeDate(inputDate: String?): String {
        Log.e("TAG", "changeDate: $inputDate",)
        return if (inputDate?.contains("T") == true) {
            val gap = inputDate.replace("-", ".").split("T")
            val time = gap[1].split(":")
            "${gap[0]}  ${time[0]}:${time[1]}"
        } else {
            inputDate.toString()
        }
    }

    fun changeAmount(amount: String): String {
        return DecimalFormat("#,###").format(amount.toInt())
    }

    fun timePickerText(hour: Int, minute: Int): String {
        val myHour = if (hour < 10) "0$hour" else "$hour"
        val myMinute = if (minute < 10) "0$minute" else "$minute"
        return "$myHour : $myMinute"
    }

    fun myLocalDateTimeFuntion(myYear : Int, myMonth : Int, myDay : Int): LocalDateTime? {
        return LocalDateTime.of(
            myYear, myMonth, myDay,
            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
        )
    }

    fun dateReplace(date : String): List<String> {
        var gap = date
        if(date.contains("T"))
            gap = StringUtil.changeDate(gap)
        return gap.split(".","  ")
    }

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

    fun PriceFormat(price : String): String? {
        if (price.isEmpty()) { return "" }
        val numericValue = try { price.toInt()
        } catch (e: NumberFormatException) { return price }
        return DecimalFormat("#,###").format(numericValue)
    }

    fun dateNow(): LocalDate {
        return LocalDate.now()
    }

    fun datePickerMonth(month: Int): String {
        var myMonth: String
        if (month < 10) myMonth = "0${month + 1}"
        else myMonth = "${month + 1}"
        return myMonth
    }

    fun datePickerDay(day: Int): String {
        var myDay: String
        if (day < 10) myDay = "0${day}"
        else myDay = "${day}"
        return myDay
    }

    //TODO 예외처리 필요함
    fun amountCheck(price: String, cardAmount: String):Boolean{
        return price.replace(",","").toInt() <= cardAmount.replace(",","").toInt()
    }
}