package com.example.receiptcareapp.viewModel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.RecyclerShowData
import com.example.domain.model.receive.DomainReceiveAllData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody

/**
 * 2023-01-31
 * pureum
 */


class FragmentViewModel : ViewModel(){

    init {
        Log.e("TAG", ": FragmentViewModel FragmentViewModel FragmentViewModel FragmentViewModel FragmentViewModel FragmentViewModel", )
    }


    private val _image = MutableLiveData<Uri>()
    val image : LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri){
        _image.value = img
    }

    private val _showLocalData = MutableLiveData<RecyclerShowData?>()
    val showLocalData : LiveData<RecyclerShowData?>
        get() = _showLocalData
    fun myShowLocalData(data: RecyclerShowData?){
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