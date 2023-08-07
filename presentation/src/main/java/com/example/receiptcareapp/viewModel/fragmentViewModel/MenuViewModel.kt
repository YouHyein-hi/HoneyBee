package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.data.manager.PreferenceManager
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.dto.LoginData
import com.example.receiptcareapp.dto.TimeData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    application: Application,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(application) {

    fun putPush(onoff: Boolean) {
        preferenceManager.putPush(onoff)
    }

    fun getPush(): Boolean? {
        return preferenceManager.getPush()
    }

    fun putTime(hour : Int, minute: Int){
        preferenceManager.putHour(hour)
        preferenceManager.putMinute(minute)
    }

    fun getTime() : TimeData = TimeData(preferenceManager.getHour(), preferenceManager.getMinute())

    fun timePickerText(hour: Int, minute: Int): String {
        val myHour = if (hour < 10) "0$hour" else "$hour"
        val myMinute = if (minute < 10) "0$minute" else "$minute"
        return "$myHour : $myMinute"
    }

}