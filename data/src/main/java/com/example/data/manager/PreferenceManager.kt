package com.example.data.manager

import android.content.Context
import javax.inject.Inject

/**
 * 2023-06-26
 * pureum
 */

class PreferenceManager @Inject constructor(
    context: Context
) {
    private val sharedPreference = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun putLogin(id: String){
        sharedPreference.edit().putString("sharedPreference_id", id).apply()
    }

    fun putPassword(pw: String){
        sharedPreference.edit().putString("sharedPreference_pw",pw).apply()
    }

    fun getLogin(): String?{
        return sharedPreference.getString("sharedPreference_id",null)
    }

    fun getPassword(): String? {
        return sharedPreference.getString("sharedPreference_pw",null)
    }

    fun clearAll(){
        sharedPreference.edit().remove("sharedPreference_id").apply()
        sharedPreference.edit().remove("sharedPreference_pw").apply()
//        sharedPreference.edit().clear().commit()
    }
}