package com.example.receiptcareapp.fragment.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.DomainReceiveAllData
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

    private val _showLocalData = MutableLiveData<DomainRoomData?>()
    val showLocalData : LiveData<DomainRoomData?>
        get() = _showLocalData
    fun myShowLocalData(data: DomainRoomData?){
        _showLocalData.value = data
    }

    private val _showServerData = MutableLiveData<DomainReceiveAllData?>()
    val showServerData : LiveData<DomainReceiveAllData?>
        get() = _showServerData
    fun myShowServerData(data: DomainReceiveAllData?){
        _showServerData.value = data
    }


}