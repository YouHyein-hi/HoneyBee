package com.example.receiptcareapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityLoginBinding
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it)}) {

    private val viewModel : LoginActivityViewModel by viewModels()
    var backPressedTime : Long = 0

    override fun initData() {
    }

    override fun initUI() {
        supportActionBar?.hide()
    }

    override fun initListener() {
        binding.button.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            viewModel.requestLogin(binding.editEmail.text.toString(), binding.editPw.text.toString())
            viewModel.requestLogin("1234@email.com", "1234")
        }
    }

    override fun initObserver() {
        viewModel.response.observe(this){response->
            Log.e("TAG", "initObserver: $response", )
            if(response=="success") {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else showShortToast("로그인에 실패하였습니다.")
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            finish()
        }
        showShortToast("한번 더 클릭 시 종료됩니다.")
        backPressedTime = System.currentTimeMillis()
    }
}