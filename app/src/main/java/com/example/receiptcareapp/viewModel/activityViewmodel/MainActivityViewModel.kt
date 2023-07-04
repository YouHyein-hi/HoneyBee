package com.example.receiptcareapp.viewModel.activityViewmodel

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.UpdateCardData
import com.example.domain.model.UpdateData
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.receive.DomainUpadateData
import com.example.domain.model.receive.DomainUpdateCardData
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
    application: Application,
    private val retrofitUseCase: RetrofitUseCase,
    private val roomUseCase: RoomUseCase,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(application) {
    private var waitTime = 5000L
    //이렇게 쓰면 메모리 누수가 일어난다는데 왜??
    // viewModel 의 lifecycle은 activity보다 길기 때문에 activity context를 참조하게되면 메모리 누수가 발생함.
    // activity가 회전할 activity는 초기화가 되고, viewModel은 유지되는데,
    // 이때 viewModel은 초기화 이전 activity의 context를 참조하고 있기 때문에 충돌 및 예외가 발생할 수 있음.
    // context를 참조하는것 이외에, 함수로 넘겨받아 사용하는것도 타이밍에 따라 문제 발생 가능함
    // 따라서 올바른 context 활용법이 필요.
    // 방법 1. activityViewModel을 상속받아 viewmodel을 구성한는 방법
    // (기존방법은 activityContext 참조였으나, ActivityContext를 참조하는방법임)
    // 방법 2. util의 APP클레스에 DI로 Context를 선언해주는 방법
    //    var myCotext: Context? = application



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

    // TODO ShowPictureFragment에만 들어가는 코드인데 ShowPictureViewModel에 옮길까
    //서버에 데이터 전송 기능
    fun sendData(sendData: AppSendData) {
        Log.e("TAG", "sendData: $sendData", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {
                val result = retrofitUseCase.sendDataUseCase(
                    DomainSendData(
                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
                        storeName = MultipartBody.Part.createFormData(
                            "storeName",
                            sendData.storeName
                        ),
                        date = MultipartBody.Part.createFormData("billSubmitTime", sendData.billSubmitTime),
                        amount = MultipartBody.Part.createFormData("amount", sendData.amount.replace(",","")),
                        picture = compressEncodePicture(sendData.picture)
                    )
                )
                Log.e("TAG", "sendData 응답 : $result ")

                if (result != "0") { //TODO uid != "0"이 아니라 실패했을 때 서버에서 받아오는 메세지로 조건식 바꾸자!!
                    var splitData =""
                    if(sendData.billSubmitTime.contains("-") && sendData.billSubmitTime.contains("T") && sendData.billSubmitTime.contains(":")){
                        splitData = dateTimeToString(sendData.billSubmitTime)
                    }
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
                    insertRoomData(
                        DomainRoomData(
                            cardName = sendData.cardName,
                            amount = sendData.amount,
                            storeName = sendData.storeName,
                            billSubmitTime = splitData,
                            file = sendData.picture.toString(),
                            uid = result
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

    //로컬 데이터 재전송
    fun resendData(sendData:AppSendData, beforeUid: String){
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "resendData: $sendData", )
            withTimeoutOrNull(waitTime) {
                val serverResult = retrofitUseCase.sendDataUseCase(
                    DomainSendData(
                        cardName = MultipartBody.Part.createFormData("cardName", sendData.cardName),
                        storeName = MultipartBody.Part.createFormData("storeName", sendData.storeName),
                        date = MultipartBody.Part.createFormData("billSubmitTime", sendData.billSubmitTime),
                        amount = MultipartBody.Part.createFormData("amount", sendData.amount.replace(",","")),
                        picture = compressEncodePicture(sendData.picture)
                    )
                )
                Log.e("TAG", "sendData 응답 : $serverResult ")
                if (serverResult != "0") {
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
                    val roomResult = roomUseCase.updateData( DomainRoomData(
                        uid = beforeUid,
                        cardName = sendData.cardName,
                        amount = sendData.amount,
                        billSubmitTime = dateTimeToString(sendData.billSubmitTime),
                        storeName = sendData.storeName,
                        file = sendData.picture.toString(),
                        )
                    )
                    Log.e("TAG", "resendData room result : $roomResult", )
                } else {
                    Log.e("TAG", "sendData: 실패입니다!")
                    _connectedState.postValue(ConnectedState.CONNECTING_FALSE)
                    Exception("오류! 전송 실패.")
                }
            } ?: throw SocketTimeoutException()
        }
    }

    private fun stringToDateTime(date:String):String{
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

    private fun dateTimeToString(date:String): String{
        val myList = date.split("-","T",":")
        return "${myList[0]}년 ${myList[1]}월 ${myList[2]}일 ${myList[3]}시 ${myList[4]}분"
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

    // TODO RecyclerFragment에만 들어가는 코드인데 RecyclerViewModel에 옮길까
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

    // TODO RecyclerShowFragment에만 들어가는 코드인데 RecyclerShowViewModel에 옮길까
    fun deleteServerData(id: Long) {
        Log.e("TAG", "deleteServerData: 들어감", )
        _connectedState.postValue(ConnectedState.CONNECTING)
        _serverJob.postValue(CoroutineScope(exceptionHandler).async {
            withTimeoutOrNull(waitTime) {
                val gap = retrofitUseCase.deleteServerData(id)
                Log.e("TAG", "deleteServerData return: $gap")
                _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
            }?: throw SocketTimeoutException()
        })
    }

    // TODO ChangeDialog에만 들어가는 코드인데 ChangeViewModel에 옮길까
    //서버 데이터 업데이트
    fun updateServerData(sendData: UpdateData, uid : String) {
        Log.e("TAG", "changeServerData: $sendData", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            withTimeoutOrNull(waitTime) {

                val localData = LocalDateTime.parse(sendData.billSubmitTime)
                Log.e("TAG", "updateServerData: $localData", )

                val result = retrofitUseCase.updateDataUseCase(
                    DomainUpadateData(
                        id = uid.toLong(),
                        cardName = sendData.cardName,
                        storeName = sendData.storeName,
                        billSubmitTime = localData,
                        amount = sendData.amount.replace(",","")
                    )
                )
                Log.e("TAG", "sendData 응답 : $result ")
                if(result.status == "200")
                    _connectedState.postValue(ConnectedState.CONNECTING_SUCCESS)
                else{
                    Exception("오류! 전송 실패")
                }
            } ?: throw SocketTimeoutException()
        }
    }

    fun updateCardData(updateCardData : UpdateCardData){
        Log.e("TAG", "updateCardData: ${updateCardData}", )
        _connectedState.value = ConnectedState.CONNECTING
        _serverJob.value = CoroutineScope(exceptionHandler).launch {
            Log.e("TAG", "updateCardData: 훔냠냠", )
            withTimeoutOrNull(waitTime) {
                val result = retrofitUseCase.updateCardDateUseCase(
                    DomainUpdateCardData(
                        id = updateCardData.id,
                        cardName = updateCardData.cardName,
                        cardAmount = updateCardData.cardAmount
                    )
                )
                Log.e("TAG", "updateCardData result: ${result}",)

                if(result.status == "200"){
                    _connectedState.postValue(ConnectedState.CARD_CONNECTING_SUCCESS)
                    receiveServerCardData()
                }
                else{
                    Exception("오류! 전송 실패")
                }


            }?:throw SocketTimeoutException()
        }
//        retrofitUseCase.updateCardDateUseCase()
    }

    // TODO RecyclerShowFragment에만 들어가는 코드인데 RecyclerShowViewModel
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

    fun compressEncodePicture(uri:Uri): MultipartBody.Part{
        val file = File(absolutelyPath(uri, getApplication()))
        val compressFile = compressImageFile(file)
        val requestFile = compressFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }


    fun clearAll(){
        preferenceManager.clearAll()
    }

    fun bitmapToUri(activity: Activity, bitmap: Bitmap?): Uri {
        Log.e("TAG", "bitmapToUri: $bitmap", )
        val file = File(activity.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file)
    }
}
