//package com.example.receiptcareapp.viewModel.dialogViewModel
//
//import android.app.Application
//import android.content.Context
//import android.database.Cursor
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Matrix
//import android.media.ExifInterface
//import android.net.Uri
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.ArrayAdapter
//import androidx.lifecycle.ViewModel
//import com.example.domain.model.UpdateData
//import com.example.domain.model.local.DomainRoomData
//import com.example.domain.model.receive.DomainUpadateData
//import com.example.domain.model.send.AppSendData
//import com.example.domain.model.send.DomainSendData
//import com.example.domain.usecase.card.GetCardListUseCase
//import com.example.domain.usecase.data.InsertDataUseCase
//import com.example.domain.usecase.data.UpdateDataUseCase
//import com.example.domain.usecase.room.InsertDataRoomUseCase
//import com.example.receiptcareapp.State.ConnectedState
//import com.example.receiptcareapp.base.BaseViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withTimeoutOrNull
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.net.SocketTimeoutException
//import java.time.LocalDateTime
//import javax.inject.Inject
//
//class ChangeViewModel @Inject constructor(
//    application: Application,
//    private val insertDataUseCase: InsertDataUseCase,
//    private val insertDataRoomUseCase: InsertDataRoomUseCase,
//    private val updateDataUseCase: UpdateDataUseCase,
//    private val getCardListUseCase: GetCardListUseCase
//) : BaseViewModel(application){
//
//    init { Log.e("TAG", "ChangeViewModel", ) }
//
//    fun DateReplace(date : String): List<String> {
//        return date.replace(" ", "").split("년", "월", "일", "시", "분", "초")
//    }
//
//    fun myLocalDateTimeFuntion(myYear : Int, myMonth : Int, myDay : Int): LocalDateTime? {
//        return LocalDateTime.of(
//            myYear, myMonth, myDay,
//            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
//        )
//    }
//
//    fun splitColon(text : String): List<String> {
//        return text.split(" : ")
//    }
//
//    fun AdapterPosition(adapter: ArrayAdapter<String>, dataCardName: String): Int {
//    // position, adapter,
//        for (i in 0 until adapter.count) {
//            val item = adapter.getItem(i)
//            if (item!!.startsWith(dataCardName)) {
//                return i
//            }
//        }
//        return -1
//    }
//
//    //서버 데이터 재전송
//
//    //로컬 데이터 재전송
//    fun updateLocalBillData(sendData: AppSendData, beforeUid: String){
//        CoroutineScope(exceptionHandler).launch {
//            Log.e("TAG", "resendData: $sendData", )
//            withTimeoutOrNull(waitTime) {
//                val serverResult = insertDataUseCase(
//                    DomainSendData(
//                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
//                        storeName = MultipartBody.Part.createFormData("storeName", sendData.storeName),
//                        date = MultipartBody.Part.createFormData("billSubmitTime", sendData.billSubmitTime),
//                        amount = MultipartBody.Part.createFormData("amount", sendData.amount.replace(",","")),
//                        picture = compressEncodePicture(sendData.picture)
//                    )
//                )
//                Log.e("TAG", "sendData 응답 : $serverResult ")
//                if (serverResult != "0") {
//                    val roomResult = insertDataRoomUseCase(
//                        DomainRoomData(
//                        uid = beforeUid,
//                        cardName = sendData.cardName,
//                        amount = sendData.amount,
//                        billSubmitTime = dateTimeToString(sendData.billSubmitTime),
//                        storeName = sendData.storeName,
//                        file = sendData.picture.toString(),
//                    )
//                    )
//                    Log.e("TAG", "resendData room result : $roomResult", )
//                } else {
//                    Log.e("TAG", "sendData: 실패입니다!")
//                    Exception("오류! 전송 실패.")
//                }
//            } ?: throw SocketTimeoutException()
//        }
//    }
//
//    // TODO ChangeDialog에만 들어가는 코드인데 ChangeViewModel에 옮길까
//    //서버 데이터 업데이트
//    fun updateServerBillData(sendData: UpdateData, uid : String) {
//        Log.e("TAG", "changeServerData: $sendData $uid", )
//        CoroutineScope(exceptionHandler).launch {
//            withTimeoutOrNull(waitTime) {
//
//                val localData = LocalDateTime.parse(sendData.billSubmitTime)
//                Log.e("TAG", "updateServerData: $localData", )
//
//                val result = updateDataUseCase(
//                    DomainUpadateData(
//                        id = uid.toLong(),
//                        cardName = sendData.cardName,
//                        storeName = sendData.storeName,
//                        billSubmitTime = localData,
//                        amount = sendData.amount.replace(",","")
//                    )
//                )
//                Log.e("TAG", "sendData 응답 : $result ")
//                if(result.status == "200")
//                else{
//                    Exception("오류! 전송 실패")
//                }
//            } ?: throw SocketTimeoutException()
//        }
//    }
//
//    //여러 Fragment에서 사용되는 함수
//    fun getServerCardData() {
//        CoroutineScope(exceptionHandler).launch {
//            withTimeoutOrNull(waitTime) {
//                val gap = getCardListUseCase()
//                Log.e("TAG", "receiveCardData: $gap")
//                _cardData.postValue(gap)
//            }?:throw SocketTimeoutException()
//        }
//    }
//
//    fun compressEncodePicture(uri: Uri): MultipartBody.Part{
//        val file = File(absolutelyPath(uri, getApplication()))
//        val compressFile = compressImageFile(file)
//        val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData("file", file.name, requestFile)
//    }
//
//    fun compressImageFile(file: File): File {
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true
//
//        BitmapFactory.decodeFile(file.absolutePath, options)
//
//        val resize = if(file.length() > 1000000) 5 else 1
//
//        options.inJustDecodeBounds = false
//        options.inSampleSize = resize
//
//        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
//
//        val rotatedBitmap = rotateImageIfRequired(bitmap, file.absolutePath)
//
//        val outputFile = File.createTempFile("compressed_", ".jpg")
//        try {
//            val outputStream = FileOutputStream(outputFile)
//            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return outputFile
//    }
//
//    fun rotateImageIfRequired(bitmap: Bitmap, imagePath: String): Bitmap {
//        val exif = ExifInterface(imagePath)
//        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//
//        return when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
//            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
//            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
//            else -> bitmap
//        }
//    }
//
//    private fun dateTimeToString(date:String): String{
//        val myList = date.split("-","T",":")
//        return "${myList[0]}년 ${myList[1]}월 ${myList[2]}일 ${myList[3]}시 ${myList[4]}분"
//    }
//
//    //절대경로로 변환
//    fun absolutelyPath(path: Uri?, context: Context?): String {
//        Log.e("TAG", "absolutelyPath: $path", )
//        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        val c: Cursor? = context?.contentResolver?.query(path!!, proj, null, null, null)
//        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        c?.moveToFirst()
//        val result = c?.getString(index!!)
//        Log.e("TAG", "absolutelyPath proj: $proj", )
//        Log.e("TAG", "absolutelyPath c: $c", )
//        Log.e("TAG", "absolutelyPath index: $index", )
//        Log.e("TAG", "absolutelyPath result: $result", )
//        return result!!
//    }
//
//    fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap {
//        val matrix = Matrix()
//        matrix.postRotate(degree)
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }
//
//    //TODO initObserver()에 있는 position 부분 ViewModel로 빼버리자~!
//
//}