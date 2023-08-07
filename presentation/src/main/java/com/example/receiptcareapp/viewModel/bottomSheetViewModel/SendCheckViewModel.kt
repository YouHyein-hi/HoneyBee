package com.example.receiptcareapp.viewModel.bottomSheetViewModel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.data.InsertDataUseCase
import com.example.domain.usecase.room.InsertDataRoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 2023-08-02
 * pureum
 */
//class SendCheckViewModel @Inject constructor(
//    application: Application,
//    private val insertDataUseCase: InsertDataUseCase,
//    private val insertDataRoomUseCase: InsertDataRoomUseCase,
//):BaseViewModel(application) {
//}