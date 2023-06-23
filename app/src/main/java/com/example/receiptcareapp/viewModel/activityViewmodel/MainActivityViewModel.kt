package com.example.receiptcareapp.viewModel.activityViewmodel

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainResendAllData
import com.example.domain.model.send.AppSendCardData
import com.example.domain.model.send.AppSendData
import com.example.domain.model.send.DomainSendCardData
import com.example.domain.model.send.DomainSendData
import com.example.domain.usecase.RetrofitUseCase
import com.example.domain.usecase.RoomUseCase
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.dto.RecyclerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.net.SocketTimeoutException
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val retrofitUseCase: RetrofitUseCase,
    private val roomUseCase: RoomUseCase
) : BaseViewModel() {
    private var waitTime = 5000L
    //이렇게 쓰면 메모리 누수가 일어난다는데 왜??
    var myCotext: Context? = null

    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri) { _image.value = img }

    private val _selectedData = MutableLiveData<RecyclerData?>()
    val selectedData : LiveData<RecyclerData?>
        get() = _selectedData
    fun changeSelectedData(data: RecyclerData?){ _selectedData.value = data }
    fun putPictureData(uri: Uri?){ _selectedData.value!!.file = uri }

    //서버에서 받은 데이터 담는 박스
    private val _serverData = MutableLiveData<MutableList<DomainReceiveAllData>>()
    val serverData: LiveData<MutableList<DomainReceiveAllData>>
        get() = _serverData

    //룸에서 받은 데이터 담는 박스
    private var _roomData = MutableLiveData<MutableList<DomainRoomData>>()
    val roomData: LiveData<MutableList<DomainRoomData>>
        get() = _roomData

    //서버 연결 유무 관리
    private var _connectedState = MutableLiveData<ConnectedState>()
    val connectedState: LiveData<ConnectedState>
        get() = _connectedState
    fun changeConnectedState(connectedState: ConnectedState) { _connectedState.value = connectedState }

    // 서버 카드 전달받은 값 관리
    private var _cardData = MutableLiveData<MutableList<DomainReceiveCardData>>()
    val cardData: LiveData<MutableList<DomainReceiveCardData>>
        get() = _cardData

    // 전달받은 서버 사진 데이터 관리
    private var _picture = MutableLiveData<Bitmap?>()
    val picture: LiveData<Bitmap?>
        get() = _picture
    fun changeNullPicture(){ _picture.value=null }

    // 코루틴 값을 담아두고 원할때 취소하기
    private var _serverJob = MutableLiveData<Job>()

    //서버에 데이터 전송 기능
    fun sendData(sendData: AppSendData) {
        Log.e("TAG", "sendData: $sendData", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                var uid = "0"
//                val file = File(absolutelyPath(sendData.picture, myCotext))
//                val compressFile = compressImageFile(file)
//                val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                val myPicture = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val result = retrofitUseCase.sendDataUseCase(
                    DomainSendData(
                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
                        storeName = MultipartBody.Part.createFormData(
                            "storeName",
                            sendData.storeName
                        ),
                        date = MultipartBody.Part.createFormData("billSubmitTime", sendData.billSubmitTime),
                        amount = MultipartBody.Part.createFormData("amount", sendData.amount.replace(",","")),
                        picture = encodePicture(sendData.picture)
                    )
                )
                Log.e("TAG", "sendData 응답 : $result ")
                uid = result

                if (uid != "0") { //TODO uid != "0"이 아니라 실패했을 때 서버에서 받아오는 메세지로 조건식 바꾸자!!
                    var splitData =""
                    if(sendData.billSubmitTime.contains("-") && sendData.billSubmitTime.contains("T") && sendData.billSubmitTime.contains(":")){
                        val myList = sendData.billSubmitTime.split("-","T",":")
                        if(myList.size == 6) splitData = "${myList[0]}년 ${myList[1]}월 ${myList[2]}일 ${myList[3]}시 ${myList[4]}분"
                    }
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
                    insertRoomData(
                        DomainRoomData(
                            cardName = sendData.cardName,
                            amount = sendData.amount,
                            storeName = sendData.storeName,
                            billSubmitTime = splitData,
                            file = sendData.picture.toString(),
                            uid = uid
                        )
                    )
                } else {
                    Log.e("TAG", "sendData: 실패입니다!")
                    _connectedState.postValue(ConnectedState.CONNECTING_FALSE)
                    Exception("오류! 전송 실패.")
                }
            } ?: throw SocketTimeoutException()
        }
    }

    fun resendData(sendData:AppSendData){
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                var uid = "0"
//                val file = File(absolutelyPath(sendData.picture, myCotext))
//                val compressFile = compressImageFile(file)
//                val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                val myPicture = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val result = retrofitUseCase.sendDataUseCase(
                    DomainSendData(
                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
                        storeName = MultipartBody.Part.createFormData(
                            "storeName",
                            sendData.storeName
                        ),
                        date = MultipartBody.Part.createFormData("date", stringToDateTime(sendData.billSubmitTime)),
                        amount = MultipartBody.Part.createFormData("amount", sendData.amount.replace(",","")),
                        picture = encodePicture(sendData.picture)
                    )
                )
                Log.e("TAG", "sendData 응답 : $result ")
                uid = result

                if (uid != "0") {
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
                } else {
                    Log.e("TAG", "sendData: 실패입니다!")
                    _connectedState.postValue(ConnectedState.CONNECTING_FALSE)
                    Exception("오류! 전송 실패.")
                }
            } ?: throw SocketTimeoutException()
        }
    }

    fun stringToDateTime(date:String):String{
        val format = date.replace(" ","").split("년","월","일","시","분")
        val myLocalDateTime = LocalDateTime.of(
            format[0].toInt(),
            format[1].toInt(),
            format[2].toInt(),
            format[3].toInt(),
            format[4].toInt(),
            LocalDateTime.now().second
        )
        return myLocalDateTime.toString()
    }

    //절대경로로 변환
    fun absolutelyPath(path: Uri?, context: Context?): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context?.contentResolver?.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)
        return result!!
    }

    fun sendCardData(sendData: AppSendCardData) {
        Log.e("TAG", "sendCardData: 카드 보내기 $sendData", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                retrofitUseCase.sendCardDataUseCase(
                    DomainSendCardData(
                        cardName = sendData.cardName,
                        cardAmount = sendData.cardAmount
                    )
                )
                _connectedState.postValue(ConnectedState.CARD_CONNECTING_SUCCESS)
                receiveServerCardData()
            }?:throw SocketTimeoutException()
        }
    }

    // Server 데이터 불러오는 부분
    fun receiveServerAllData() {
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime){
                val gap = retrofitUseCase.receiveDataUseCase()
                _serverData.postValue(gap)
                _connectedState.postValue(ConnectedState.DISCONNECTED)
            } ?: throw SocketTimeoutException()
        }
    }

    fun receiveServerCardData() {
        _connectedState.postValue(ConnectedState.CONNECTING)
        _serverJob.postValue(CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = retrofitUseCase.receiveCardDataUseCase()
                Log.e("TAG", "receiveCardData: $gap")
                _cardData.postValue(gap)
                _connectedState.postValue(ConnectedState.DISCONNECTED)
            }?:throw SocketTimeoutException()
        })
    }

    fun requestServerPictureData(uid:String){
        _connectedState.postValue(ConnectedState.CONNECTING)
        _serverJob.postValue(CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = retrofitUseCase.receivePictureDataUseCase(uid)
                Log.e("TAG", "receiveServerPictureData gap : $gap", )
                _picture.postValue(gap)
                _connectedState.postValue(ConnectedState.DISCONNECTED)
            }?:throw SocketTimeoutException()
        })
    }

    fun deleteServerData(id: Long) {
        Log.e("TAG", "deleteServerData: 들어감", )
        CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val gap = retrofitUseCase.deleteServerData(id)
                Log.e("TAG", "deleteServerData return: $gap")
            }?: throw SocketTimeoutException()
        }
    }

    fun updateServerData(sendData: AppSendData, uid : String, beforeDate: String) {
        Log.e("TAG", "changeServerData: $sendData", )
        Log.e("TAG", "changeServerData: $uid", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
//                val file = File(absolutelyPath(sendData.picture, myCotext))
//                val compressFile = compressImageFile(file)
//                val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                val myPicture = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val result = retrofitUseCase.updateDataUseCase(
                    DomainResendAllData(
                        id = MultipartBody.Part.createFormData("id", uid),
                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
                        amount = MultipartBody.Part.createFormData("amount", sendData.amount.replace(",","")),
                        date = MultipartBody.Part.createFormData("billSubmitTime", sendData.billSubmitTime),
                        storeName = MultipartBody.Part.createFormData("storeName", sendData.storeName),
                        picture = encodePicture(sendData.picture)
                    )
                )
                Log.e("TAG", "sendData 응답 : $result ")
                if (result == "add success") {
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)

                    //TODO 룸 업데이트 기능 만들어서 적용하기
//                    deleteRoomData(beforeDate)
//                    insertRoomData(
//                        DomainRoomData(
//                            cardName = sendData.cardName,
//                            amount = sendData.amount,
//                            storeName = sendData.storeName,
//                            billSubmitTime = sendData.billSubmitTime,
//                            file = sendData.picture.toString(),
//                            uid = uid
//                        )
//                    )
                }
                else {
                    Log.e("TAG", "sendData: 실패입니다!")
                    _connectedState.postValue(ConnectedState.CONNECTING_FALSE)
                    Exception("오류! 전송 실패.")
                }
            } ?: throw SocketTimeoutException()
        }
    }

    fun deleteRoomData(date: String) {
        CoroutineScope(exceptionHandler).launch {
            _connectedState.postValue(ConnectedState.CONNECTING)
            val result = roomUseCase.deleteData(date)
            Log.e("TAG", "deleteRoomData result : $result", )
            //삭제 후에 데이터 끌어오기 위한 구성
            receiveAllLocalData()
            _connectedState.postValue(ConnectedState.DISCONNECTED)
        }
    }

    fun insertRoomData(domainRoomData: DomainRoomData) {
        CoroutineScope(exceptionHandler).launch {
            _connectedState.postValue(ConnectedState.CONNECTING)
            roomUseCase.insertData(domainRoomData)
            _connectedState.postValue(ConnectedState.DISCONNECTED)
        }
    }

    fun receiveAllLocalData() {
        CoroutineScope(exceptionHandler).launch {
            _connectedState.postValue(ConnectedState.CONNECTING)
            val gap = roomUseCase.getAllData()
            Log.e("TAG", "receiveAllRoomData: $gap")
            _roomData.postValue(gap)
            _connectedState.postValue(ConnectedState.DISCONNECTED)
        }
    }

    fun serverCoroutineStop() {
        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소"))
        this.setFetchStateStop()
        _connectedState.postValue(ConnectedState.DISCONNECTED)
    }

    fun hideServerCoroutineStop() {
        _serverJob.value?.cancel("코루틴 취소", InterruptedIOException("강제 취소"))
        _connectedState.postValue(ConnectedState.DISCONNECTED)
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

    fun compressImageFile(inputFile: File): File {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(inputFile.absolutePath, options)

        val resize = if(inputFile.length() > 1000000) 6 else 1

        options.inJustDecodeBounds = false
        options.inSampleSize = resize

        val bitmap = BitmapFactory.decodeFile(inputFile.absolutePath, options)

        val rotatedBitmap = rotateImageIfRequired(bitmap, inputFile.absolutePath)

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

    fun encodePicture(uri:Uri): MultipartBody.Part{
        val file = File(absolutelyPath(uri, myCotext))
        val compressFile = compressImageFile(file)
        val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
}


//    fun deleteCardData(id: Long) {
//        _connectedState.value = ConnectedState.CONNECTING
//        _serverJob.value = CoroutineScope(exceptionHandler).launch {
//            withTimeoutOrNull(waitTime) {
//                retrofitUseCase.deleteCardDataUseCase(id)
//                // 결과값을 분기문으로 관리 + 커넥트 풀어주기
//                // 성공하면 값을 불러오기
//                receiveServerCardData()
//            }?:throw SocketTimeoutException()
//        }
//    }

//    fun changeCardData(id: Long) {
//        CoroutineScope(exceptionHandler).launch {
//            withTimeoutOrNull(waitTime) {
//                val gap = roomUseCase.getAllData()
//                Log.e("TAG", "changeCardData: $gap")
////            retrofitUseCase.resendCardDataUseCase()
//            }?:throw SocketTimeoutException()
//        }
//    }
