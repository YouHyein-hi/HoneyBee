package com.example.receiptcareapp.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import com.example.data.manager.PreferenceManager
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

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(
    { ActivityLoginBinding.inflate(it) }, 
    "LoginActivity"
) {
    private val viewModel: LoginActivityViewModel by viewModels()
    private var backPressedTime: Long = 0
    private val loginData = LoginData(null,null)

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
    }
}