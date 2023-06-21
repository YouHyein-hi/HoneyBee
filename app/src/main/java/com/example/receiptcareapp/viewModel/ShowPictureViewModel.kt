package com.example.receiptcareapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class ShowPictureViewModel : ViewModel(){

    init { Log.e("TAG", "ShowPictureViewModel", ) }

    fun CommaReplaceSpace(text : String): String { return text.replace(",", "") }

    fun DateNow(): LocalDate{ return LocalDate.now() }

    fun DatePickerMonth(month : Int): String {
        var myMonth : String
        if(month < 10) myMonth = "0${month + 1}"
        else myMonth = "${month + 1}"
        return myMonth
    }

    fun DatePickerDay(day : Int): String {
        var myDay : String
        if(day < 10) myDay = "0${day}"
        else myDay = "${day}"
        return myDay
    }

    fun myLocalDateTimeFuntion(myYear : Int, myMonth : Int, myDay : Int): LocalDateTime? {
        return LocalDateTime.of(myYear, myMonth, myDay,
            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)
    }

    private val _card = MutableLiveData<Map<String, Int>>()
    val card : LiveData<Map<String, Int>>
        get() = _card
    fun takeCardData(card: Map<String, Int>){
        _card.value = card
    }

    fun spiltCardSplit(myPostion: String) : ArrayList<String>{
        return myPostion.split(" : ") as ArrayList<String>
    }

}