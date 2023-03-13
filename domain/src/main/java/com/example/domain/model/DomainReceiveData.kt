package com.example.domain.model

import android.net.Uri
import okhttp3.MultipartBody
import java.time.LocalDateTime

/**
 * 2023-02-02
 * pureum
 */
data class DomainReceiveData(
    val cardName: String,
    val amount: String,
    val date: LocalDateTime,
    val pictureName: String,
    val picture: Uri
)
