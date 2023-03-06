package com.example.receiptcareapp.fragment.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.DomainRoomData
import java.io.ByteArrayOutputStream

/**
 * 2023-01-31
 * pureum
 */

class FragmentViewModel(
    application: Application
) : AndroidViewModel(Application()){
    init {
        Log.e("TAG", ": viewMoodel start", )
    }

    private val _picture = MutableLiveData<Bitmap>()
    val picture : LiveData<Bitmap>
        get() = _picture

    private val _bytePicture = MutableLiveData<ByteArray>()
    val bytePicture : LiveData<ByteArray>
        get() = _bytePicture
    fun takePicture(bitmap:Bitmap){

        //bytearray 변환시키기
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        _bytePicture.value = outputStream.toByteArray()
        Log.e("TAG", "takePicture: ${_bytePicture.value}", )
        _picture.value = bitmap
    }

    private val _image = MutableLiveData<Uri>()
    val image : LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri){
        Log.e("TAG", "takeImage: ${image.value}", )
        _image.value = img
        Log.e("TAG", "takeImage: ${image.value}", )
    }




    /**
     1 : CameraFragment
     2 : GalleryFragment
     **/
    private val _pageNum = MutableLiveData<Int>()
    val pageNum : LiveData<Int>
        get() = _pageNum
    fun takePage(pageNum: Int){
        _pageNum.value = pageNum
    }

    private val _showData = MutableLiveData<DomainRoomData>()
    val showData : LiveData<DomainRoomData>
        get() = _showData
    fun myShowData(list:DomainRoomData){
        _showData.value = list
        Log.e("TAG", "myShowData: $showData", )
    }


//    private val _bitMap = MutableLiveData<Bitmap>()
//    val bitMap : LiveData<Bitmap>
//        get() = _bitMap
//    fun myBitMap(bitmap: Bitmap) {
//        _bitMap.value = bitmap
//        Log.e("TAG", "myBitMap: $bitMap", )
//    }
}