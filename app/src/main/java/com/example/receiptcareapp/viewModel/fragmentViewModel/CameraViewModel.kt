package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class CameraViewModel : ViewModel(){

    init { Log.e("TAG", "CameraViewModel", ) }

    private val _photoUri = MutableLiveData<Uri>()
    val photoUri: LiveData<Uri> = _photoUri

    fun checkPermission(context: Context, activity : Activity, permissions: Array<out String>, requestCode : Int): Boolean {
        Log.e("TAG", "checkPermission: CameraViewModel", )
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

    fun dispatchTakePictureIntentExViewModel(activity : Activity): Intent {
        Log.e("TAG", "dispatchTakePictureIntentExViewModel: CameraViewModel", )
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri: Uri? = createImageUri(activity, "JPEG_${timeStamp}_", "image/jpeg")
        _photoUri.value = uri!!
        return takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }

    fun createImageUri(activity : Activity, filename:String, mimeType:String): Uri? {
        Log.e("TAG", "createImageUri: ", )
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }
        return activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
}