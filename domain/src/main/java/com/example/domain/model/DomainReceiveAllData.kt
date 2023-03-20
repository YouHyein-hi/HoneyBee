package com.example.domain.model

import android.net.Uri
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class DomainReceiveAllData(
    val cardName: String,
    val amount: String,
    val date: String,
    val pictureName: String,
    val picture: Uri
)
