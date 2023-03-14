package com.example.data.local

import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.LocalDateTime

/**
 * 2023-03-09
 * pureum
 */
@ProvidedTypeConverter
class LocalDateTimeConverter(private val gson: Gson) {
    @TypeConverter
    fun localDateTimeToJson(gap : LocalDateTime):String{
        return gson.toJson(gap)
    }
    @TypeConverter
    fun jsonToLocalDateTime(gap : String):LocalDateTime{
        return gson.fromJson(gap, LocalDateTime::class.java)
    }
}