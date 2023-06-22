package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel(){

    init { Log.e("TAG", "GalleryViewModel", ) }

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

    fun CallGallery(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        return intent
    }
}