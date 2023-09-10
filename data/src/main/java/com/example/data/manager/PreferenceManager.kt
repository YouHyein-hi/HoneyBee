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

    //TODO 하나하나 넣는것보다 데이터클래스를 넣는 방법으로 해야함
    fun putLogin(id: String){
        sharedPreference.edit().putString("sharedPreference_id", id).apply()
    }

    fun putPassword(pw: String){
        sharedPreference.edit().putString("sharedPreference_pw",pw).apply()
    }

    fun getLogin(): String? {
        return sharedPreference.getString("sharedPreference_id",null)
    }

    fun getPassword(): String? {
        return sharedPreference.getString("sharedPreference_pw",null)
    }

    fun clearLoginPassword(){
        sharedPreference.edit().remove("sharedPreference_id").apply()
        sharedPreference.edit().remove("sharedPreference_pw").apply()
    }



    //Auth Token Control
    fun putAccessToken(accessToken : String){
        sharedPreference.edit().putString("access_token", accessToken).apply()
    }

    fun putRefreshToken(refreshToken : String){
        sharedPreference.edit().putString("refresh_token", refreshToken).apply()
    }

    fun putUserName(name: String){
        sharedPreference.edit().putString("name", name).apply()
    }

    fun putUserRight(right: String){
        sharedPreference.edit().putString("right", right).apply()
    }

    fun putAuthData(accessToken: String, refreshToken: String, name: String, right: String){
        sharedPreference.edit().putString("access_token", accessToken).apply()
        sharedPreference.edit().putString("refresh_token", refreshToken).apply()
        sharedPreference.edit().putString("name", name).apply()
        sharedPreference.edit().putString("right", right).apply()
    }

    fun getAccessToken(): String?{
        return sharedPreference.getString("access_token", null)
    }

    fun getRefreshToken(): String?{
        return sharedPreference.getString("refresh_token", null)
    }

    fun getUserName(): String?{
        return sharedPreference.getString("name", null)
    }

    fun getUserRight(): String?{
        return sharedPreference.getString("right", null)
    }

    fun clearAccessToken(){
        sharedPreference.edit().remove("access_token").apply()
    }

    fun clearRefreshToken(){
        sharedPreference.edit().remove("refresh_token").apply()
    }

    fun clearUserName(){
        sharedPreference.edit().remove("name").apply()
    }

    fun clearUserRight(){
        sharedPreference.edit().remove("right").apply()
    }

    fun clearAuthAll(){
        sharedPreference.edit().remove("access_token").apply()
        sharedPreference.edit().remove("refresh_token").apply()
        sharedPreference.edit().remove("name").apply()
        sharedPreference.edit().remove("right").apply()
    }




    //Date Control
    //TODO 하나하나 넣는것보다 데이터클래스를 넣는 방법으로 해야함
    fun putPush(onoff : Boolean){
        sharedPreference.edit().putBoolean("sharedPreference_push", onoff).apply()
    }

    fun getPush() : Boolean?{
        return sharedPreference.getBoolean("sharedPreference_push", false)
    }

    fun putHour(hour : Int){
        sharedPreference.edit().putInt("sharedPreference_pushHour", hour).apply()
    }

    fun putMinute(minute : Int){
        sharedPreference.edit().putInt("sharedPreference_pushMinute", minute).apply()
    }

    fun getHour() : Int?{
        return sharedPreference.getInt("sharedPreference_pushHour", 17)
    }

    fun getMinute() : Int?{
        return sharedPreference.getInt("sharedPreference_pushMinute", 0)
    }
}