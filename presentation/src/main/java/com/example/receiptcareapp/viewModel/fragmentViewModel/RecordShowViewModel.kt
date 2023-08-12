package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.app.Activity
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
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.UpdateData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainServerReponse
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.data.DeleteDataUseCase
import com.example.domain.usecase.data.GetPictureDataUseCase
import com.example.domain.usecase.data.InsertDataUseCase
import com.example.domain.usecase.data.UpdateDataUseCase
import com.example.domain.usecase.room.DeleteDataRoomUseCase
import com.example.domain.usecase.room.GetDataListRoomUseCase
import com.example.domain.usecase.room.InsertDataRoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-06-21
 * pureum
 */
@HiltViewModel
class RecordShowViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val deleteDataRoomUseCase: DeleteDataRoomUseCase,
    private val getPictureDataUseCase: GetPictureDataUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    private val getCardListUseCase: GetCardListUseCase,
    private val insertDataUseCase: InsertDataUseCase,
    private val insertDataRoomUseCase: InsertDataRoomUseCase
) : BaseViewModel() {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<ResponseState>()
    val response : LiveData<ResponseState> get() = _response

    // 서버 카드 전달받은 값 관리
    private var _cardData = MutableLiveData<MutableList<DomainReceiveCardData>?>()
    val cardData: LiveData<MutableList<DomainReceiveCardData>?>
        get() = _cardData

    private var _picture = MutableLiveData<Bitmap?>()
    val picture : LiveData<Bitmap?> get(){
        Log.e("TAG", "@@@@picture ${_picture.value}: ", )
        return _picture
    }


    // TODO ChangeDialog에만 들어가는 코드인데 ChangeViewModel에 옮길까
    //서버 데이터 업데이트
    fun updateServerBillData(sendData: UpdateData, uid: String) {
        CoroutineScope(exceptionHandler).launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                updateResponse(
                    updateDataUseCase(
                        DomainUpadateData(
                            id = uid.toLong(),
                            cardName = sendData.cardName,
                            storeName = sendData.storeName,
                            billSubmitTime = LocalDateTime.parse(sendData.billSubmitTime),
                            amount = sendData.amount.replace(",", "")
                        )
                    ),
                    ResponseState.UPDATE_SUCCESS
                )
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    //로컬 데이터 재전송
    fun updateLocalBillData(sendData: AppSendData, uid: String) {
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                imsiUpdateResponse(
                    insertDataUseCase(
                        DomainSendData(
                            cardName = MultipartBody.Part.createFormData(
                                "cardName",
                                sendData.cardName
                            ),
                            storeName = MultipartBody.Part.createFormData(
                                "storeName",
                                sendData.storeName
                            ),
                            date = MultipartBody.Part.createFormData(
                                "billSubmitTime",
                                sendData.billSubmitTime
                            ),
                            amount = MultipartBody.Part.createFormData(
                                "amount",
                                sendData.amount.replace(",", "")
                            ),
                            picture = compressEncodePicture(sendData.picture)
                        )
                    ),
                    ResponseState.UPDATE_SUCCESS,
                    uid,
                    sendData
                )
                Log.e("TAG", "sendData 응답 : $response ")
            } ?: throw SocketTimeoutException()
        }
    }
    fun deleteServerBillData(id: Long) {
        Log.e("TAG", "deleteServerData: 들어감")
        CoroutineScope(exceptionHandler).async {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                deleteDataUseCase(id)
                isLoading.postValue(false)
                //TODO 이거 updateResponse로 빼야함
                _response.postValue(ResponseState.DELETE_SUCCESS)
            } ?: throw SocketTimeoutException()
        }
    }

    fun deleteRoomBillData(date: String) {
        CoroutineScope(exceptionHandler).launch {
            isLoading.postValue(true)
            val result = deleteDataRoomUseCase(date)
            Log.e("TAG", "deleteRoomData result : $result")
            isLoading.postValue(false)
            //삭제 후에 데이터 끌어오기 위한 구성
//            getLocalAllBillData()
        }
    }
    // 이 기능을 따로 빼야할듯
    private fun insertRoomData(domainRoomData: DomainRoomData) {
        CoroutineScope(exceptionHandler).launch {
            insertDataRoomUseCase(domainRoomData)
            _response.postValue(ResponseState.SUCCESS)
        }
    }

    private fun updateResponse(response: DomainServerReponse, type: ResponseState){
        when(response.status){
            "200" -> _response.postValue(type)
            else -> _response.postValue(ResponseState.FALSE)
        }
    }

    //TODO 들어오는 값이 통일되면 하나로 합치기 + 응답값에 맞춰서 움직여야함
    private fun imsiUpdateResponse(response: String, type: ResponseState, uid: String, sendData: AppSendData){
        when(response) {
            else -> {
                _response.postValue(type)
                deleteRoomBillData(uid)
                insertRoomData(
                    DomainRoomData(
                        uid,
                        sendData.cardName,
                        sendData.amount,
                        sendData.billSubmitTime,
                        sendData.storeName,
                        sendData.picture.toString()
                    )
                )
            }
//            "200" -> _response.postValue(type)
//            else -> _response.postValue(ResponseState.FALSE)
        }
    }

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        CoroutineScope(exceptionHandler).launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardData.postValue(getCardListUseCase())
                isLoading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }

    fun getServerPictureData(uid:String){
        CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "getServerPictureData: ", )
            withTimeoutOrNull(waitTime) {
                loading.postValue(true)
                _picture.postValue(getPictureDataUseCase(uid))

                loading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }

    fun myLocalDateTimeFuntion(myYear : Int, myMonth : Int, myDay : Int): LocalDateTime? {
        return LocalDateTime.of(
            myYear, myMonth, myDay,
            LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
        )
    }

    fun AdapterPosition(adapter: ArrayAdapter<String>, dataCardName: String): Int {
        // position, adapter,
        for (i in 0 until adapter.count) {
            val item = adapter.getItem(i)
            if (item!!.startsWith(dataCardName)) {
                return i
            }
        }
        return -1
    }

    //TODO 이건 두군대서 쓰는함수,, 아래친구들도 전부
    private fun dateTimeToString(date:String): String{
        val myList = date.split("-","T",":")
        return "${myList[0]}년 ${myList[1]}월 ${myList[2]}일 ${myList[3]}시 ${myList[4]}분"
    }

    fun splitColon(text : String): List<String> {
        return text.split(" : ")
    }

    fun dateReplace(date : String): List<String> {
        return date.replace(" ", "").split("년", "월", "일", "시", "분", "초")
    }

    fun compressEncodePicture(uri:Uri): MultipartBody.Part{
        val file = File(absolutelyPath(uri, application))
        val compressFile = compressImageFile(file)
        val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    fun compressImageFile(file: File): File {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(file.absolutePath, options)

        val resize = if(file.length() > 1000000) 5 else 1

        options.inJustDecodeBounds = false
        options.inSampleSize = resize

        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

        val rotatedBitmap = rotateImageIfRequired(bitmap, file.absolutePath)

        val outputFile = File.createTempFile("compressed_", ".jpg")
        try {
            val outputStream = FileOutputStream(outputFile)
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return outputFile
    }

    fun rotateImageIfRequired(bitmap: Bitmap, imagePath: String): Bitmap {
        val exif = ExifInterface(imagePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    //절대경로로 변환
    fun absolutelyPath(path: Uri?, context: Context?): String {
        Log.e("TAG", "absolutelyPath: $path", )
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context?.contentResolver?.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)
        Log.e("TAG", "absolutelyPath proj: $proj", )
        Log.e("TAG", "absolutelyPath c: $c", )
        Log.e("TAG", "absolutelyPath index: $index", )
        Log.e("TAG", "absolutelyPath result: $result", )
        return result!!
    }

//    fun getLocalAllBillData() {
//        CoroutineScope(exceptionHandler).launch {
//            val gap = getRoomDataListUseCase()
//            Log.e("TAG", "receiveAllRoomData: $gap")
//            _roomData.postValue(gap)
//        }
//    }


//    fun bitmapToUri(activity: Activity, bitmap: Bitmap): Uri {
//        val file = File(activity.cacheDir, "temp_image.jpg")
//        val outputStream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        outputStream.flush()
//        outputStream.close()
//        return Uri.fromFile(file)
//    }

//    fun uriToBitmap(activity:Activity, uri:Uri):Bitmap{
//        return ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.contentResolver,uri))
//    }
}