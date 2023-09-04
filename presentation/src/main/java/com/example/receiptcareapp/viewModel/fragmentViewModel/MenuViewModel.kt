package com.example.receiptcareapp.viewModel.fragmentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.receiptcareapp.base.BaseViewModel
import com.example.domain.model.ui.dateTime.TimeData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : BaseViewModel("MenuViewModel") {

    private val _pushTime = MutableLiveData<TimeData>()
    val pushTime: LiveData<TimeData>
        get() = _pushTime

    fun putPush(onoff: Boolean) {
        preferenceManager.putPush(onoff)
    }

    fun getPush(): Boolean? {
        return preferenceManager.getPush()
    }

    fun putTime(hour: Int, minute: Int) {
        //생성자
        preferenceManager.putHour(hour)
        preferenceManager.putMinute(minute)
        _pushTime.value = TimeData(hour, minute)
    }

    fun getTime() : TimeData = TimeData(preferenceManager.getHour(), preferenceManager.getMinute())
}