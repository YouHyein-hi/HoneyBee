package com.example.domain.util

import android.widget.SpinnerAdapter
import com.example.domain.model.remote.receive.card.CardSpinnerData
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


object StringUtil {
    fun changeDate(inputDate: String?): String {
        return if (inputDate?.contains("T") == true) {
            val gap = inputDate.replace("-", ".").split("T")
            val time = gap[1].split(":")
            "${gap[0]}  ${time[0]}:${time[1]}"
        } else {
            inputDate.toString()
        }
    }

    fun changeAmount(amount: String): String =
        DecimalFormat("#,###").format(amount.toInt())

    fun timePickerText(hour: Int, minute: Int): String {
        val myHour = if (hour < 10) "0$hour" else "$hour"
        val myMinute = if (minute < 10) "0$minute" else "$minute"
        return "$myHour : $myMinute"
    }

    fun myLocalDateTimeFunction(myYear : Int, myMonth : Int, myDay : Int): LocalDateTime? =
        LocalDateTime.of(
            myYear, myMonth, myDay,
            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
        )

    fun myLocalDateFunction(myYear : Int, myMonth : Int, myDate : Int) : LocalDate? =
        LocalDate.of(myYear, myMonth, myDate)

    fun dateReplaceDot(date : String): List<String> {
        var gap = date
        if(date.contains("T"))
            gap = changeDate(gap)
        return gap.split(".","  ")
    }

    fun dateReplaceLine(date : String): List<String> {
        var gap = date
        if(date.contains("T"))
            gap = changeDate(gap)
        return gap.split("-","  ")
    }

    fun commaReplaceSpace(text : String): String =
        text.replace(",", "")

    fun priceFormat(price : String): String? {
        if (price.isEmpty()) { return "" }
        val numericValue = try { price.toInt()
        } catch (e: NumberFormatException) { return price }
        return DecimalFormat("#,###").format(numericValue)
    }

    fun dateNow(): LocalDate =
        LocalDate.now()

    fun datePickerMonth(month: Int): String =
        if (month < 10) "0${month + 1}"
        else "${month + 1}"


    fun datePickerDay(day: Int): String =
        if (day < 10) "0${day}"
        else "$day"

    fun amountCheck(price: String, cardAmount: String):Boolean =
        price.replace(",","").toInt() <= cardAmount.replace(",","").toInt()

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
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(date)
    }
}