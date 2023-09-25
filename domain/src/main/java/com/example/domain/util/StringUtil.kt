package com.example.domain.util

import android.preference.PreferenceManager
import android.util.Log
import android.widget.SpinnerAdapter
import com.example.domain.model.remote.receive.card.CardSpinnerData
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


object StringUtil {
    fun changeDate(inputDate: String?): String {
        Log.e("TAG", "changeDate inputDate: $inputDate", )
        return if (inputDate?.contains("T") == true) {
            val gap = inputDate.replace("-", ".").split("T")
            Log.e("TAG", "changeDate: $gap", )
            val time = gap[1].split(":")
            Log.e("TAG", "changeDate time: $time", )
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

    fun myLocalDateFuntion(myYear : Int, myMonth : Int, myDate : Int) : LocalDate?{
        return LocalDate.of(myYear, myMonth, myDate)
    }

    fun dateReplaceDot(date : String): List<String> {
        var gap = date
        if(date.contains("T"))
            gap = StringUtil.changeDate(gap)
        return gap.split(".","  ")
    }

    fun dateReplaceLine(date : String): List<String> {
        var gap = date
        if(date.contains("T"))
            gap = StringUtil.changeDate(gap)
        return gap.split("-","  ")
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

    fun amountCheck(price: String, cardAmount: String):Boolean{
        return price.replace(",","").toInt() <= cardAmount.replace(",","").toInt()
    }

    fun findPositionByCardName(cardName: String, adapter: SpinnerAdapter): Int {
        for (i in 0 until adapter.count) {
            val item = adapter.getItem(i) as? CardSpinnerData
            if (item != null && item.name.startsWith(cardName)) {
                return i
            }
        }
        return -1
    }

    fun datetimeToDate(dateTime: String) : String{
        // 원래 문자열을 Date 객체로 변환
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val date = inputFormat.parse(dateTime)

        // 새로운 형식으로 변환
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(date)
    }
}