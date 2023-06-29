package com.example.receiptcareapp.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityLoginBinding
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it) }) {

    private val viewModel: LoginActivityViewModel by viewModels()
    var backPressedTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
    private val GALLERY = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val CAMERA_CODE = 98
    private val GALLERY_CODE = 1010

    override fun initData() {
    }

    override fun initUI() {
        supportActionBar?.hide()
    }

    override fun initListener() {
        //TODO 뭔가 카메라, 갤러리 사이의 텀이 이상해!!!!!!!!!
        checkPermission(CAMERA, CAMERA_CODE)

        binding.button.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            viewModel.requestLogin(binding.editEmail.text.toString(), binding.editPw.text.toString())
            viewModel.requestLogin("1234@email.com", "1234")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun initObserver() {
        viewModel.response.observe(this) { response ->
            Log.e("TAG", "initObserver: $response")
            when (response) {
                "로그인 성공" -> {
                    showShortToast("로그인 성공.")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                "존재하지 않는 계정입니다." -> {
                    showShortToast("존재하지 않는 계정입니다.")
                }
                "이메일 또는 비밀번호가 틀립니다." -> {
                    showShortToast("이메일 또는 비밀번호가 틀립니다.")
                }
                else -> {
                    showShortToast("알 수 없는 오류입니다.")
                }
            }

        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            finish()
        }
        showShortToast("한번 더 클릭 시 종료됩니다.")
        backPressedTime = System.currentTimeMillis()
    }


    private fun checkPermission(permissions: Array<out String>, requestCode : Int) {
        // 마시멜로 버전 이후
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.e("TAG", "onRequestPermissionsResult: 에 접근", )
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionsString : String = " "

        Log.e("TAG", "onRequestPermissionsResult: $requestCode", )

        when(requestCode){
            CAMERA_CODE -> {
                Log.e("TAG", "onRequestPermissionsResult: 카메라 권한 접근", )
                permissionsString = "카메라"
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("TAG", "${permissionsString} 권한이 허용됐습니다!")
                    showShortToast("${permissionsString} 권한이 허용됐습니다!")

                    handler.postDelayed({
                        Log.e("TAG", "initListener: 딜레이 2초 지남", )
                        checkPermission(GALLERY, GALLERY_CODE)
                    }, 2000)
                } else {
                    Log.e("TAG", "${permissionsString}는 서비스에 필요한 권한입니다.권한에 동의해주세요.")
                    showShortToast("${permissionsString}는 서비스에 필요한 권한입니다.\n권한에 동의해주세요.")
                    handler.postDelayed({
                        checkPermission(CAMERA, CAMERA_CODE)
                    }, 1500)

                    handler.postDelayed({
                        Log.e("TAG", "initListener: 딜레이 2초 지남", )
                        checkPermission(GALLERY, GALLERY_CODE)
                    }, 2000)
                }



            }
            GALLERY_CODE -> {
                Log.e("TAG", "onRequestPermissionsResult: 겔러리 권한 접근", )
                permissionsString = "갤러리"
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("TAG", "${permissionsString} 권한이 허용됐습니다!")
                    showShortToast("${permissionsString} 권한이 허용됐습니다!")
                } else {
                    Log.e("TAG", "${permissionsString}는 서비스에 필요한 권한입니다.권한에 동의해주세요.")
                    showShortToast("${permissionsString}는 서비스에 필요한 권한입니다.\n권한에 동의해주세요.")
                    handler.postDelayed({
                        checkPermission(GALLERY, GALLERY_CODE)
                    }, 1500)
                }
            }


        }
    }
}