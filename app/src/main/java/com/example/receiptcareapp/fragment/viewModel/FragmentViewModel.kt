package com.example.receiptcareapp.fragment.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.receive.DomainReceiveAllData
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

    private val _showLocalData = MutableLiveData<DomainReceiveAllData?>()
    val showLocalData : LiveData<DomainReceiveAllData?>
        get() = _showLocalData
    fun myShowLocalData(data: DomainReceiveAllData?){
        Log.e("TAG", "myShowLocalData: $data", )
        _showLocalData.value = data
    }

    private val _showServerData = MutableLiveData<DomainReceiveAllData?>()
    val showServerData : LiveData<DomainReceiveAllData?>
        get() = _showServerData
    fun myShowServerData(data: DomainReceiveAllData?){
        Log.e("TAG", "myShowServerData: $data", )
        _showServerData.value = data
    }

    private var _startGap = MutableLiveData<String>()
    val startGap : LiveData<String>
        get() = _startGap
    fun changeStartGap(gap:String){
        _startGap.value = gap
    }

    private val _card = MutableLiveData<Map<String, Int>>()
    val card : LiveData<Map<String, Int>>
        get() = _card
    fun takeCardData(card: Map<String, Int>){
        _card.value = card
    }

}