package com.example.receiptcareapp.viewModel

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream

/**
 * 2023-06-21
 * pureum
 */
class RecyclerShowFragment : ViewModel() {

    fun bitmapToUri(activity: Activity, bitmap: Bitmap): Uri {
        val file = File(activity.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file)
    }

//    fun uriToBitmap(activity:Activity, uri:Uri):Bitmap{
//        return ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.contentResolver,uri))
//    }
}