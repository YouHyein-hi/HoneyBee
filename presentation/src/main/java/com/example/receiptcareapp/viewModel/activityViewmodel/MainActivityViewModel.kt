package com.example.receiptcareapp.viewModel.activityViewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.manager.PreferenceManager
import com.example.domain.model.receive.NoticeData
import com.example.domain.model.send.*
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
) : BaseViewModel() {
    //이렇게 쓰면 메모리 누수가 일어난다는데 왜??
    // viewModel 의 lifecycle은 activity보다 길기 때문에 activity context를 참조하게되면 메모리 누수가 발생함.
    // activity가 회전할 activity는 초기화가 되고, viewModel은 유지되는데,
    // 이때 viewModel은 초기화 이전 activity의 context를 참조하고 있기 때문에 충돌 및 예외가 발생할 수 있음.
    // context를 참조하는것 이외에, 함수로 넘겨받아 사용하는것도 타이밍에 따라 문제 발생 가능함
    // 따라서 올바른 context 활용법이 필요.
    // 방법 1. activityViewModel을 상속받아 viewmodel을 구성한는 방법
    // (기존방법은 activityContext 참조였으나, ActivityContext를 참조하는방법임)
    // 방법 2. util의 APP클레스에 DI로 Context를 선언해주는 방법
//    var myCotext: Context?

    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri>
        get() = _image
    fun takeImage(img: Uri) { _image.value = img }

    private val _selectedData = MutableLiveData<RecyclerData?>()
    val selectedData : LiveData<RecyclerData?>
        get() = _selectedData
    fun changeSelectedData(data: RecyclerData?){ _selectedData.value = data }

    lateinit var selectedNoticeData : NoticeData

    fun clearAll(){
        preferenceManager.clearAll()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
