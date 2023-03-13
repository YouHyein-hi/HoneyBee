package com.example.receiptcareapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.MainViewModel
import com.example.receiptcareapp.viewModel.base.FetchState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val activityViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        activityViewModel.myCotext = this

        supportActionBar?.hide()

        activityViewModel.fetchState.observe(this) {
            //프로그래스 바 풀어주기
            activityViewModel.isConnected("pass")
            val message = when (it.second) {
                FetchState.BAD_INTERNET -> "BAD_INTERNET 오류"
                FetchState.PARSE_ERROR -> "PARSE_ERROR 오류"
                FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
                FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "이미 값이 저장되어있습니다."
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> "연결 시간이 초과되었습니다."
                    else -> "저장 안된 오류!  ${it.first.message} "
                }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "onCreate: $message", )
        }
    }
}
