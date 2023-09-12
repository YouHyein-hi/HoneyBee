package com.example.data.util

import android.util.Log
import com.example.data.BuildConfig
import com.example.data.manager.PreferenceManager
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import okhttp3.Headers
import javax.inject.Inject

/**
 * 2023-08-30
 * pureum
 */
class HeaderManager @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    operator fun invoke(headers: Headers): Boolean {
        return try {
            val accessToken = headers["Authorization"]!!.replace("Bearer ", "")
            val refreshToken = headers["RefreshToken"]!!
            Log.e("TAG", "accessToken: $accessToken", )
            Log.e("TAG", "refreshToken: $refreshToken", )

            //형식 검증
            if (accessToken.split(".").size != 3 || refreshToken.split(".").size != 3) false

            //엑세스 토큰 시그니처 검증
            if (!verifyToken(accessToken)) false

            //리프레시 토큰 시그니처 검증
            if (!verifyToken(refreshToken)) false

            //토큰 저장
            clearToken()
            saveToken("Bearer $accessToken", refreshToken)

            //엑세스 토큰 까서 아이디 또는 이름 저장하기

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun verifyToken(accessToken: String): Boolean {
        return try {
            val decodingKey = Decoders.BASE64.decode(BuildConfig.SECRET_KEY)
            Log.e("TAG", "decodingKey: $decodingKey", )
            val signingKey = Keys.hmacShaKeyFor(decodingKey)
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(accessToken)
                .body
            true
        } catch (e: Exception) { false }
    }

    private fun clearToken(){
        preferenceManager.removeAccessToken()
        preferenceManager.removeRefreshToken()
    }
    private fun saveToken(accessToken: String, refreshToken: String){
        preferenceManager.putAccessToken(accessToken)
        preferenceManager.putRefreshToken(refreshToken)
    }
}