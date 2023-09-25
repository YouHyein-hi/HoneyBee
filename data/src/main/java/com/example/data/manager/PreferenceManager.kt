package com.example.data.manager

import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import androidx.core.content.edit
import javax.inject.Inject

/**
 * 2023-06-26
 * pureum
 */

class PreferenceManager @Inject constructor(
    private val sharedPreference: SharedPreferences
) {

    init {

    }
    //login
    fun putEmail(id: String) = sharedPreference.edit().putString("sharedPreference_id", id).apply()
    fun getEmail(): String? = sharedPreference.getString("sharedPreference_id",null)
    fun removeEmail() = sharedPreference.edit().remove("sharedPreference_id").apply()

    fun putAutoEmailCheckBox(boolean: Boolean) = sharedPreference.edit().putBoolean("sharedPreference_auto_email", boolean).apply()
    fun getAutoEmailCheckBox(): Boolean = sharedPreference.getBoolean("sharedPreference_auto_email", false)
    fun removeAutoEmailCheckBox() = sharedPreference.edit().remove("sharedPreference_auto_email").apply()

    fun putAutoLoginCheckBox(boolean: Boolean) = sharedPreference.edit().putBoolean("sharedPreference_auto_login", boolean).apply()
    fun getAutoLoginCheckBox(): Boolean = sharedPreference.getBoolean("sharedPreference_auto_login", false)
    fun removeAutoLoginCheckBox() = sharedPreference.edit().remove("sharedPreference_auto_login").apply()


    //Auth Token Control
    fun putAccessToken(accessToken : String) = sharedPreference.edit().putString("access_token", accessToken).apply()
    fun getAccessToken(): String? = sharedPreference.getString("access_token", null)
    fun removeAccessToken() = sharedPreference.edit().remove("access_token").apply()

    fun putRefreshToken(refreshToken : String) = sharedPreference.edit().putString("refresh_token", refreshToken).apply()
    fun getRefreshToken(): String? = sharedPreference.getString("refresh_token", null)
    fun removeRefreshToken() = sharedPreference.edit().remove("refresh_token").apply()

    fun putUserRight(right: String) = sharedPreference.edit().putString("right", right).apply()
    fun getUserRight(): String? = sharedPreference.getString("right", null)
    fun removeUserRight() = sharedPreference.edit().remove("right").apply()

    fun putAuthTime(time: String) = sharedPreference.edit().putString("auth_time", time).apply()
    fun getAuthTime(): String? = sharedPreference.getString("auth_time", null)
    fun removeAuthTime() = sharedPreference.edit().remove("auth_time").apply()

    fun removeAuth(){
        sharedPreference.edit().remove("access_token").apply()
        sharedPreference.edit().remove("refresh_token").apply()
        sharedPreference.edit().remove("right").apply()
        sharedPreference.edit().remove("auth_time").apply()
    }


    //Date Control
    fun putPush(onoff : Boolean) = sharedPreference.edit().putBoolean("sharedPreference_push", onoff).apply()
    fun getPush() : Boolean = sharedPreference.getBoolean("sharedPreference_push", false)
    fun removePush() = sharedPreference.edit().remove("sharedPreference_push").apply()

    fun putHour(hour : Int) = sharedPreference.edit().putInt("sharedPreference_pushHour", hour).apply()
    fun getHour() : Int = sharedPreference.getInt("sharedPreference_pushHour", 17)
    fun removeHour() = sharedPreference.edit().remove("sharedPreference_pushHour").apply()

    fun putMinute(minute : Int) = sharedPreference.edit().putInt("sharedPreference_pushMinute", minute).apply()
    fun getMinute() : Int = sharedPreference.getInt("sharedPreference_pushMinute", 0)
    fun removeMinute() = sharedPreference.edit().remove("sharedPreference_pushMinute").apply()
}