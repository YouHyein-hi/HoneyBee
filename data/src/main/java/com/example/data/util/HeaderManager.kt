package com.example.data.util

import android.util.Base64.URL_SAFE
import android.util.Log
import com.example.data.manager.PreferenceManager
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import java.util.Base64.getEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * 2023-08-30
 * pureum
 */
class HeaderManager @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    operator fun invoke(encryptionAccessToken: String, encryptionRefreshToken: String): Boolean {
        Log.e("TAG", "encryptionAccessToken: $encryptionAccessToken ", )

//        Log.e("TAG", "accessToken: $encryptionAccessToken ", )
//        Log.e("TAG", "refreshToken: $encryptionRefreshToken ", )

        preferenceManager.putAccessToken(encryptionAccessToken)
        preferenceManager.putRefreshToken(encryptionRefreshToken)

        val accessToken = encryptionAccessToken.replace("Bearer ","").let { it.split(".") }
        val refreshToken = encryptionRefreshToken.let { it.split(".") }

//        Log.e("TAG", "accessToken: $accessToken ", )
//        Log.e("TAG", "refreshToken: $refreshToken ", )

        //JWT 형식 검증
        if (accessToken.size != 3){ throw Exception("비정상적인 헤더") }

        accessTokenHandler(accessToken)

        return true

//        val hash = header.getString("hash")
//
//        if(signature == decodingWithSecretKey(payload, hash)){
//            preferenceManager.clearAuthAll()
//            preferenceManager.putAuthData(
//                accessToken = payload.getString("accessToken"),
//                refreshToken = payload.getString("refreshToken"),
//                name = payload.getString("userName"),
//                right = payload.getString("userRight")
//            )
//            return true
//        }
//        return false
    }
}

private fun accessTokenHandler(accessToken: List<String>){
    var secretKey = "b18cca61839fa36db64e461c23867d98ea1eeaec7524b9f334314c0d5f0ed96b9feba377d41c0c93d054d66aafcc55145648b026ad2bdd9f697f653111ea72f7"
    Log.e("TAG", "encode secretKey: ${Base64.getEncoder().encodeToString(secretKey.toByteArray())}")
    val headerToPayLoad = accessToken[0] + "." + accessToken[1]
    val header = JSONObject(String(android.util.Base64.decode(accessToken[0], URL_SAFE)))
    val payLoad = JSONObject(String(android.util.Base64.decode(accessToken[1], URL_SAFE)))
    val signature = String(android.util.Base64.decode(accessToken[2], URL_SAFE))

    val secretKeyBytes = secretKey.toByteArray(StandardCharsets.UTF_8)
    val key: Key = SecretKeySpec(secretKeyBytes, "HmacSHA512")

    val mac = Mac.getInstance("HmacSHA512")
    mac.init(key)
    val calculatedSignatureBytes = mac.doFinal((headerToPayLoad).toByteArray(StandardCharsets.UTF_8))
    val calculatedSignature1 = Base64.getUrlEncoder().withoutPadding().encodeToString(calculatedSignatureBytes)
    Log.e("TAG", "headerToPayLoad: $headerToPayLoad ", )
    Log.e("TAG", "signature: ${accessToken[2]}")
    Log.e("TAG", "d signature: $signature")
    Log.e("TAG", "calculatedSignature: $calculatedSignature1", )
}

private fun refreshTokenHandler(refresh: List<String>){

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