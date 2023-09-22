package com.example.receiptcareapp.ui.activity

import android.content.Context
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.util.FetchStateHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    {ActivityMainBinding.inflate(it)},
    "MainActivity"
) {
    private val activityViewModel: MainActivityViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
        supportActionBar?.hide()
    }

    override fun initListener() {}

    override fun initObserver() {
        activityViewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
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
