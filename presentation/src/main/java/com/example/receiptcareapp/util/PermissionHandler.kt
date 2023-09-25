package com.example.receiptcareapp.util

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast

class PermissionHandler (private val context: Context) {


    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        for (i in permissions.indices) {
            val grantResult = grantResults[i]
            when (permissions[i]) {
                android.Manifest.permission.CAMERA -> {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        showToast("카메라 권한이 허용됐습니다!")
                    }
                }
                android.Manifest.permission.READ_MEDIA_IMAGES -> {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        showToast("갤러리 권한이 허용됐습니다!")
                    }
                }
                android.Manifest.permission.POST_NOTIFICATIONS ->{
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        showToast("알림 권한이 허용됐습니다!")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}