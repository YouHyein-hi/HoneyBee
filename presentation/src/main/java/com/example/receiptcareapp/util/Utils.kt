package com.example.receiptcareapp.util

import com.example.domain.util.StringUtil
import java.text.DecimalFormat
import java.time.LocalDateTime

object Utils {

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

}