package com.example.receiptcareapp.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.example.receiptcareapp.base.BaseActivity
import com.example.receiptcareapp.databinding.ActivityLoginBinding
import com.example.receiptcareapp.dto.LoginData
import com.example.receiptcareapp.ui.adapter.PermissionHandler
import com.example.receiptcareapp.ui.dialog.Permissiond_Dialog
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it) }, "LoginActivity") {
    private val viewModel: LoginActivityViewModel by viewModels()
    private lateinit var permissionHandler: PermissionHandler
    private var backPressedTime: Long = 0
    private val loginData = LoginData(null,null)
    private val ALL_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val ALL_PERMISSIONS_CODE = 123

    override fun initData() {
        nextActivity()

        var getLogin = viewModel.getLoginData()
        if(getLogin.id != null){
            nextActivity()
        }

        if (!checkAllPermissionsGranted(ALL_PERMISSIONS)) {
            permissionDialog()
        }
        else{
            showShortToast("권한 있음")
        }
        permissionHandler = PermissionHandler(this@LoginActivity)
    }

    override fun initUI() {
        supportActionBar?.hide()
        binding.loginEmail.setText("1234@email.com")
        binding.loginPassword.setText("1234")
    }

    override fun initListener() {
        binding.loginButton.setOnClickListener {
            loginData.id = binding.loginEmail.text.toString()
            loginData.pw = binding.loginPassword.text.toString()
            downKeyBoard()
            with(loginData){
                if(id.isNullOrEmpty())
                    showShortToast("아이디를 입력해주세요.")
                else if(pw.isNullOrEmpty())
                    showShortToast("비밀번호를 입력해주세요.")
                else viewModel.requestLogin(
                    binding.loginEmail.text.toString().replace(" ",""),
                    binding.loginPassword.text.toString().replace(" ","")
                )
            }
        }
    }

    override fun initObserver() {
        viewModel.loading.observe(this){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        //응답 성공 시
        viewModel.response.observe(this) { response ->
            Log.e("TAG", "initObserver: $response")
            when (response.status) {
                "200" -> {
                    viewModel.putLoginData(loginData)
                    nextActivity()
                }
                else -> {showShortToast("로그인 실패")}
            }
        }

        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun nextActivity(){
        showShortToast("환영합니다.")
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
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

    private fun downKeyBoard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    //권한 Dialog
    private fun permissionDialog() {
        val permissionDialog = Permissiond_Dialog()
        permissionDialog.setOnDismissListener(object : Permissiond_Dialog.OnDismissListener {
            override fun onDialogDismissed() {
                checkPermission(ALL_PERMISSIONS, ALL_PERMISSIONS_CODE)
            }
        })
        permissionDialog.show(supportFragmentManager, "permissiondDialog")
    }

    // 권한 체크
    private fun checkAllPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    //권한 관련
    private fun checkPermission(permissions: Array<out String>, requestCode : Int) {
        // 마시멜로 버전 이후
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    // TODO 이 부분을 ViewModel 로 빼고 ViewModel 에서는 Enum Class로 값 관리하기
    // TODO 여기선 observer 만하는게 어떨지
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.e("TAG", "onRequestPermissionsResult: 에 접근",)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // 두 개의 배열 파라미터를 모두 전달합니다.

        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}