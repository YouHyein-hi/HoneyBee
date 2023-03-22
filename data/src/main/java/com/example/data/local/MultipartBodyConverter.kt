package com.example.data.local

import android.net.Uri
import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-03-09
 * pureum
 */
//@ProvidedTypeConverter
//class MultipartBodyConverter(private val gson: Gson) {
//    @TypeConverter
//    fun UriToJson(gap : Uri):String{
//        return gson.toJson(gap)
//    }
//    @TypeConverter
//    fun jsonToUri(gap : String): Uri {
//        return gson.fromJson(gap, Uri::class.java)
//    }
//}