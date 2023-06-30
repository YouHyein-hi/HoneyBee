package com.example.receiptcareapp.viewModel.dialogViewModel

import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class ChangeViewModel : ViewModel(){

    init { Log.e("TAG", "ChangeViewModel", ) }

    fun DateReplace(date : String): List<String> {
        return date.replace(" ", "").split("년", "월", "일", "시", "분", "초")
    }

    fun myLocalDateTimeFuntion(myYear : Int, myMonth : Int, myDay : Int): LocalDateTime? {
        return LocalDateTime.of(
            myYear, myMonth, myDay,
            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
        )
    }

    fun SplitColon(text : String): List<String> {
        return text.split(" : ")
    }

    fun AdapterPosition(adapter: ArrayAdapter<String>, dataCardName: String): Int {
    // position, adapter,
        for (i in 0 until adapter.count) {
            val item = adapter.getItem(i)
            if (item!!.startsWith("$dataCardName ")) {
                return i
            }
        }
        return -1
    }



    //TODO initObserver()에 있는 position 부분 ViewModel로 빼버리자~!

}