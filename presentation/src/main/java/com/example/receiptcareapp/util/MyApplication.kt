package com.example.receiptcareapp.util

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.example.data.manager.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

/**
 * 2023-01-31
 * pureum
 */
@HiltAndroidApp
// 액티비티 시작전에 작동함
class MyApplication:Application(
) {
    companion object {
        lateinit var right: String
    }

}