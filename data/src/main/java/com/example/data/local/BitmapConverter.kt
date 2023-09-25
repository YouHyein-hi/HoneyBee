package com.example.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

/**
 * 2023-05-08
 * pureum
 */
//@ProvidedTypeConverter
//class BitmapConverter {
//    @TypeConverter
//    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
//        Log.e("TAG", "bitmapToByteArray: $bitmap", )
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        return stream.toByteArray()
//    }
//    @TypeConverter
//    fun byteArrayToBitmap(bytes : ByteArray):Bitmap{
//        Log.e("TAG", "byteArrayToBitmap: $bytes", )
//        var decode = Base64.decode(bytes, Base64.DEFAULT)
//        var bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size)
//        Log.e("TAG", "byteArrayToBitmap: $bitmap", )
//        return bitmap
//    }
//}