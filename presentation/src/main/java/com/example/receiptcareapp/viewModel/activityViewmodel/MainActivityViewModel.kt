package com.example.receiptcareapp.viewModel.activityViewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.receive.notice.NoticeData
import com.example.receiptcareapp.base.BaseViewModel
import com.example.receiptcareapp.dto.RecyclerData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 2023-01-31
 * pureum
 */

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : BaseViewModel("MainActivityViewModel") {

    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri) { _image.value = img }

    private val _selectedData = MutableLiveData<RecyclerData?>()
    val selectedData : LiveData<RecyclerData?>
        get() = _selectedData
    fun changeSelectedData(data: RecyclerData?){ _selectedData.value = data }
    fun removeSelectedData(){ _selectedData.value = null }

    lateinit var selectedNoticeData : NoticeData

    fun clearAll(){
        preferenceManager.clearAll()
    }
}
