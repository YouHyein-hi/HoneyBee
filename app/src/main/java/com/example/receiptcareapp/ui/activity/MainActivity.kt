package com.example.receiptcareapp.ui.activity

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.util.FetchState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {
//    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val activityViewModel: MainActivityViewModel by viewModels()

    override fun initData() {
        activityViewModel.myCotext = applicationContext
    }

    override fun initUI() {
        supportActionBar?.hide()
    }

    override fun initListener() {}

    override fun initObserver() {
        activityViewModel.fetchState.observe(this) {
            //프로그래스 바 풀어주기
            activityViewModel.changeConnectedState(ConnectedState.CONNECTING_FALSE)
            // 에러라는것을 알리기
            val message = when (it.second) {
                FetchState.BAD_INTERNET -> "BAD_INTERNET 오류"
                FetchState.PARSE_ERROR -> "PARSE_ERROR 오류"
                FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
                FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "이미 값이 저장되어있습니다."
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> " 연결 시간이 초과되었습니다."
                FetchState.STOP -> ""
                FetchState.HIDE_STOP -> ""
                else -> "저장 안된 오류!  ${it.first.message} "
            }
            if(message.isNotEmpty()) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "onCreate: $message")
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        if (currentFocus is EditText) { currentFocus!!.clearFocus() }
        return super.dispatchTouchEvent(ev)
    }


}
