package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.data.manager.PreferenceManager
import com.example.receiptcareapp.base.BaseViewModel
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
}