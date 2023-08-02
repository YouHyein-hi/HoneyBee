package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class SendBillDataViewModel : ViewModel() {

    init {
        Log.e("TAG", "ShowPictureViewModel")
    }

    fun commaReplaceSpace(text: String): String {
        return text.replace(",", "")
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

    fun myLocalDateTimeFuntion(myYear: Int, myMonth: Int, myDay: Int): LocalDateTime? {
        return LocalDateTime.of(
            myYear, myMonth, myDay,
            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
        )
    }




}