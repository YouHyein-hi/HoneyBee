package com.example.data.util

import com.example.data.manager.PreferenceManager
import com.google.gson.JsonObject
import org.json.JSONObject
import javax.inject.Inject

/**
 * 2023-08-30
 * pureum
 */
class HeaderManager @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    operator fun invoke(encryptedToken: String?): Boolean {

        val token = encryptedToken?.let { decodingHeader(it).split(".") }

        //복호화 한 것 검증
        if (token?.size != 3) {
            throw Exception("비정상적인 헤더")
        }

        val header = JSONObject(token[0])
        val payload = JSONObject(decodingPayload(token[1]))
        val signature = token[2]

        val hash = header.getString("hash")

        if(signature == decodingWithSecretKey(payload, hash)){
            preferenceManager.clearAuthAll()
            preferenceManager.putAuthData(
                accessToken = payload.getString("accessToken"),
                refreshToken = payload.getString("refreshToken"),
                name = payload.getString("userName"),
                right = payload.getString("userRight")
            )
            return true
        }
        return false
    }
}

private fun decodingHeader(encryptedHeaders: String): String {
    //Base64 복호화하기

    //secrete 키로 복호화하기
    return ""
}

private fun decodingPayload(payload: String): String{
    //secrete 키로 복호화하기
    return ""
}

private fun decodingWithSecretKey(payload: JSONObject, hash:String): String{
    //hash 알고리즘으로 암호화
    return ""
}