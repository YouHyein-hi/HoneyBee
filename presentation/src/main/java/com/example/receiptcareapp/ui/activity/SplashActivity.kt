package com.example.receiptcareapp.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity :
    BaseActivity<ActivitySplashBinding>({ ActivitySplashBinding.inflate(it) }, "SplashActivity") {

    override fun initData() {}

    override fun initUI() {
        supportActionBar?.hide()

        ObjectAnimator.ofFloat(this.binding.title, View.ALPHA, 0f, 1f).apply {
            duration = 1500
            start()
        }
        ObjectAnimator.ofFloat(this.binding.background, View.ALPHA, 0f, 1f).apply {
            duration = 1500
            start()
        }

        lifecycleScope.launch {
            delay(2300L)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun initListener() {}

    override fun initObserver() {}
}