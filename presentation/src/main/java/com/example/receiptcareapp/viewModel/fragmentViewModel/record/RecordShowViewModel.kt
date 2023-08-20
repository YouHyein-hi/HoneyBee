package com.example.receiptcareapp.viewModel.fragmentViewModel.record

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
import com.example.domain.model.UpdateData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.ServerCardData
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.receive.ServerUidData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.bill.DeleteDataUseCase
import com.example.domain.usecase.bill.GetPictureDataUseCase
import com.example.domain.usecase.bill.InsertDataUseCase
import com.example.domain.usecase.bill.UpdateDataUseCase
import com.example.domain.usecase.room.DeleteDataRoomUseCase
import com.example.domain.usecase.room.InsertDataRoomUseCase
import com.example.domain.usecase.room.UpdateRoomData
import com.example.domain.util.changeDate
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.ui.dialog.ChangeDialog
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.util.RoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
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
    private val getPictureDataUseCase: GetPictureDataUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    private val getCardListUseCase: GetCardListUseCase,
    private val insertDataUseCase: InsertDataUseCase,
    private val insertDataRoomUseCase: InsertDataRoomUseCase,
    private val deleteDataRoomUseCase: DeleteDataRoomUseCase,
    private val updateRoomData: UpdateRoomData
    ) : BaseViewModel() {

    val loading: MutableLiveData<Boolean> get() = isLoading

    private var _response = MutableLiveData<Pair<ResponseState,ServerUidData?>>()
    val response : LiveData<Pair<ResponseState,ServerUidData?>> get() = _response

    private val _roomState = MutableLiveData<RoomState>()
    val roomState: LiveData<RoomState> get() = _roomState

    // 서버 카드 전달받은 값 관리
    private var _cardList = MutableLiveData<ServerCardData?>()
    val cardList: LiveData<ServerCardData?>
        get() = _cardList

    private var _picture = MutableLiveData<Bitmap?>()
    val picture : LiveData<Bitmap?> get(){
        Log.e("TAG", "@@@@picture ${_picture.value}: ", )
        return _picture
    }

    private lateinit var savedServerData: UpdateData
    private lateinit var savedLocalData: AppSendData


    // TODO ChangeDialog에만 들어가는 코드인데 ChangeViewModel에 옮길까
    //서버 데이터 업데이트
    fun updateServerBillData(sendData: UpdateData, uid: String) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.postValue(Pair(
                        ResponseState.UPDATE_SUCCESS,
                        updateDataUseCase(
                            DomainUpadateData(
                                id = uid.toLong(),
                                cardName = sendData.cardName,
                                storeName = sendData.storeName,
                                billSubmitTime = LocalDateTime.parse(sendData.billSubmitTime),
                                amount = sendData.amount.replace(",", "").toInt()
                            )
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
            savedServerData = sendData
            isLoading.postValue(false)
        }
    }

    //로컬 데이터 재전송
    fun updateLocalBillData(sendData: AppSendData) {
        modelScope.launch {
            withTimeoutOrNull(waitTime) {
                _response.postValue(Pair(
                        ResponseState.LOCAL_UPDATE_SUCCESS,
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
                        )
                    )
                )
            } ?: throw SocketTimeoutException()
        }
    }

    fun deleteServerBillData(id: Long) {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _response.value = Pair(ResponseState.DELETE_SUCCESS, deleteDataUseCase(id))
            } ?: throw SocketTimeoutException()
            isLoading.postValue(false)
        }
    }

    fun deleteRoomBillData(date: String? = savedLocalData.billSubmitTime) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)
            val result = deleteDataRoomUseCase(date!!)
            Log.e("TAG", "deleteRoomData result : $result")
            //삭제 후에 데이터 끌어오기 위한 구성
//            getLocalAllBillData()
            _roomState.postValue(RoomState.DELETE_SUCCESS)
            isLoading.postValue(false)

        }

    }
    // 이 기능을 따로 빼야할듯
    fun insertRoomData(uid:String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            insertDataRoomUseCase(
                DomainRoomData(
                    uid = uid,
                    cardName = savedLocalData.cardName,
                    amount = savedLocalData.amount,
                    billSubmitTime = savedLocalData.billSubmitTime,
                    storeName = savedLocalData.storeName,
                    file = savedLocalData.picture.toString()
                )
            )
//            _roomState.postValue(RoomState.INSERT_SUCCESS)
        }
    }

    fun upDataRoomData(uid: String){
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
            updateRoomData(
                DomainRoomData(
                    uid = uid,
                    cardName = savedLocalData.cardName,
                    amount = savedLocalData.amount,
                    billSubmitTime = savedLocalData.billSubmitTime,
                    storeName = savedLocalData.storeName,
                    file = savedLocalData.picture.toString()
                )
            )
//            _roomState.postValue(RoomState.UPDATE_SUCCESS)
        }
    }

    //여러 Fragment에서 사용되는 함수
    fun getServerCardData() {
        modelScope.launch {
            isLoading.postValue(true)
            withTimeoutOrNull(waitTime) {
                _cardList.postValue(getCardListUseCase())
                isLoading.postValue(false)
            }?:throw SocketTimeoutException()
        }
    }

    fun getServerPictureData(uid:String){
        modelScope.launch {
            Log.e("TAG", "getServerPictureData: ", )
            withTimeoutOrNull(waitTime) {
                loading.postValue(true)
                _picture.postValue(getPictureDataUseCase(uid).picture)
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
        var gap = date
        if(date.contains("T"))
            gap = changeDate(gap)
        return gap.split(".","  ")
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