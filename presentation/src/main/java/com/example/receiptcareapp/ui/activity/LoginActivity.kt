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
import com.example.domain.model.ui.login.LoginData
import com.example.receiptcareapp.util.PermissionHandler
import com.example.receiptcareapp.ui.dialog.PermissionDialog
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
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.POST_NOTIFICATIONS
    )
    private val ALL_PERMISSIONS_CODE = 123
    private val permissionDialog: PermissionDialog by lazy { PermissionDialog() }

    override fun initData() {
        viewModel.loadGetAuthData()
        if(viewModel.getAutoLoginCheckBoxState())
            if(viewModel.checkAuthTime())
                nextActivity()

        if (!checkAllPermissionsGranted(ALL_PERMISSIONS))
            permissionDialog()

        permissionHandler = PermissionHandler(this@LoginActivity)
    }

    override fun initUI() {
        supportActionBar?.hide()
        binding.loginEmailEdit.setText(viewModel.getId()?:"")
        binding.loginAutoLoginCheckBox.isChecked = viewModel.getAutoLoginCheckBoxState()
        binding.loginSavedIdCheckBox.isChecked = viewModel.getIdCheckBoxState()
    }

    override fun initListener() {
        binding.loginLoginBtn.setOnClickListener {
            loginData.id = binding.loginEmailEdit.text.toString().replace(" ","")
            loginData.pw = binding.loginPasswordEdit.text.toString().replace(" ","")
            downKeyBoard()
            with(loginData){
                if(id.isNullOrEmpty())
                    showShortToast("아이디를 입력해주세요.")
                else if(pw.isNullOrEmpty())
                    showShortToast("비밀번호를 입력해주세요.")
                else viewModel.requestLogin(
                    loginData.id.toString().replace(" ",""),
                    loginData.pw.toString().replace(" ",""),
                    binding.loginAutoLoginCheckBox.isChecked,
                    binding.loginSavedIdCheckBox.isChecked
                )
            }
        }
    }

    override fun initObserver() {
        viewModel.loading.observe(this){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.response.observe(this) { response ->
            when (response.status) {
                "200" -> {
                    viewModel.loadGetAuthData()
                    nextActivity()
                }
                else -> {showShortToast("로그인 실패")}
            }
        }

        viewModel.fetchState.observe(this) {
            showShortToast("로그인 실패")
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

    private fun checkAllPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun checkPermission(permissions: Array<out String>, requestCode : Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    private fun permissionDialog(){
        permissionDialog.setOnDismissListener(object : PermissionDialog.OnDismissListener {
            override fun onDialogDismissed() {
                checkPermission(ALL_PERMISSIONS, ALL_PERMISSIONS_CODE)
            }
        })
        permissionDialog.show(supportFragmentManager, "permissionDialog")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}