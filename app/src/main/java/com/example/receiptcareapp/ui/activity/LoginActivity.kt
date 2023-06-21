package com.example.receiptcareapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityLoginBinding
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it)}) {

    private val activityViewModel : LoginActivityViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
        supportActionBar?.hide()
    }

    override fun initListener() {

        binding.button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun initObserver() {
    }
}