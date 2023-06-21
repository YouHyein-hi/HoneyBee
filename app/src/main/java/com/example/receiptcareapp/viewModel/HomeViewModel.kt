package com.example.receiptcareapp.viewModel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){

    init { Log.e("TAG", "HomeViewModel", ) }

    fun checkPermission(context: Context, activity : Activity, permissions: Array<out String>, requestCode : Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode)
                    return false
                }
            }
        }
        return true
    }

}