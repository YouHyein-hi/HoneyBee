package com.example.receiptcareapp.fragment.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.DomainRoomData
import okhttp3.MultipartBody

/**
 * 2023-01-31
 * pureum
 */

class FragmentViewModel : ViewModel(){

    private val _image = MutableLiveData<Uri>()
    val image : LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri){
        Log.e("TAG", "takeImage: ${image.value}", )
        _image.value = img
        Log.e("TAG", "takeImage: ${image.value}", )
    }

    private val _multiPartPicture = MutableLiveData<MultipartBody.Part>()
    val multiPartPicture : LiveData<MultipartBody.Part>
        get() = _multiPartPicture
    fun getMultiPartPicture(img: MultipartBody.Part){
        _multiPartPicture.value = img
    }

    private val _showData = MutableLiveData<DomainRoomData>()
    val showData : LiveData<DomainRoomData>
        get() = _showData
    fun myShowData(data: DomainRoomData){
        _showData.value = data
    }

    private val _card = MutableLiveData<Map<String, Int>>()
    val card : LiveData<Map<String, Int>>
        get() = _card
    fun takeCardData(card: Map<String, Int>){
        _card.value = card
    }

}