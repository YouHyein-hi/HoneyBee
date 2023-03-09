package com.example.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-03-09
 * pureum
 */
@ProvidedTypeConverter
class MultipartBodyConverter(private val gson: Gson) {
    @TypeConverter
    fun multiPartBodyToJson(gap : MultipartBody.Part):String{
        return gson.toJson(gap)
    }
    @TypeConverter
    fun jsonToMultiPartBody(gap : String): MultipartBody.Part {
        return gson.fromJson(gap, MultipartBody.Part::class.java)
    }
}