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
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import com.example.data.manager.PreferenceManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityLoginBinding
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.dto.LoginData
import com.example.receiptcareapp.util.FetchState
import com.example.receiptcareapp.viewModel.activityViewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(
    { ActivityLoginBinding.inflate(it) }, 
    "LoginActivity"
) {
    private val viewModel: LoginActivityViewModel by viewModels()
    private var backPressedTime: Long = 0
    private val loginData = LoginData(null,null)

    private val handler = Handler(Looper.getMainLooper())
    private val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
    private val GALLERY = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val CAMERA_CODE = 98
    private val GALLERY_CODE = 1010

    override fun initData() {
        if(viewModel.getLoginData().id != null){
            nextAndFinish()
            Log.e("TAG", "initData: ${viewModel.getLoginData()}", )
        }else{
            Log.e("TAG", "initData: 로그인 정보가 없음!", )
        }
    }

    override fun initUI() {
        supportActionBar?.hide()
        binding.editEmail.setText("1234@email.com")
        binding.editPw.setText("1234")

    }

    override fun initListener() {
        //TODO 뭔가 카메라, 갤러리 사이의 텀이 이상해!!!!!!!!!
        checkPermission(CAMERA, CAMERA_CODE)

        binding.button.setOnClickListener {
            loginData.id = binding.editEmail.text.toString()
            loginData.pw = binding.editPw.text.toString()
            downKeyBoard()
            with(loginData){
                if(id.isNullOrEmpty()) showShortToast("아이디를 입력해주세요.")
                else if(pw.isNullOrEmpty()) showShortToast("비밀번호를 입력해주세요.")
                else viewModel.requestLogin(binding.editEmail.text.toString(), binding.editPw.text.toString())
            }
        }
    }

    override fun initObserver() {
        viewModel.connectedState.observe(this){
            Log.e("TAG", "show onViewCreated: $it", )
            if(it==ConnectedState.CONNECTING) {
                binding.loginProgressBar.visibility = View.VISIBLE
                binding.loginView.visibility = View.VISIBLE
            }
            else{
                binding.loginProgressBar.visibility = View.INVISIBLE
                binding.loginView.visibility = View.INVISIBLE
            }
        }

        //에러 대응
        viewModel.fetchState.observe(this) {
            viewModel.changeState(ConnectedState.DISCONNECTED)
            val message = when (it.second) {
                FetchState.BAD_INTERNET -> "인터넷 연결 실패"
                FetchState.PARSE_ERROR -> "아이디 또는 비밀번호를 잘못 입력하셨습니다."
                FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
                FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "이미 값이 저장되어있습니다."
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> " 연결 시간이 초과되었습니다."
                FetchState.STOP -> ""
                FetchState.HIDE_STOP -> ""
                else -> "저장 안된 오류!  ${it.first.message} "
            }
            if(message.isNotEmpty()) showShortToast(message)
            Log.e("TAG", "onCreate: $message")
        }

        //응답 성공 시
        viewModel.response.observe(this) { response ->
            Log.e("TAG", "initObserver: $response")
            when (response.status) {
                "200" -> {
                    viewModel.putLoginData(
                        binding.editEmail.text.toString(), binding.editPw.text.toString()
                    )
                    nextAndFinish()
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        downKeyBoard()
        return true
    }

    private fun nextAndFinish(){
        showShortToast("환영합니다.")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun downKeyBoard(){
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

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