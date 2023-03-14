package com.example.receiptcareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.receiptcareapp.databinding.ActivityMainBinding
import com.example.receiptcareapp.viewModel.MainViewModel
import com.example.receiptcareapp.viewModel.base.FetchState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val activityViewModel: MainViewModel by viewModels()

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.e("TAG", "navigatePhotos: ${it.resultCode}", )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        activityViewModel.myCotext = this

        supportActionBar?.hide()

        activityViewModel.fetchState.observe(this) {
            //프로그래스 바 풀어주기
            activityViewModel.isConnected("failed")
            val message = when (it.second) {
                FetchState.BAD_INTERNET -> "BAD_INTERNET 오류"
                FetchState.PARSE_ERROR -> "PARSE_ERROR 오류"
                FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
                FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "실패! 이미 값이 저장되어있습니다."
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> "실패! 연결 시간이 초과되었습니다."
                    else -> "저장 안된 오류!  ${it.first.message} "
                }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            Log.e("TAG", "onCreate: $message", )
        }
    }
}
