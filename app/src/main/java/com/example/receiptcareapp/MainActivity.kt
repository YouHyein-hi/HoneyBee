package com.example.receiptcareapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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

        //supportActionBar?.hide()

        activityViewModel.fetchState.observe(this) {
            val message = when (it.second) {
                    FetchState.BAD_INTERNET -> "BAD_INTERNET 오류"
                    FetchState.PARSE_ERROR -> "PARSE_ERROR 오류"
                    FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
                    FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "이미 저장되어있습니다!"
                    else -> "${it.first.message} 오류"
                }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this, R.style.AppCompatAlertDialog)
            .setTitle("종료")
            .setMessage("꿀을 그만 빠시겠어요?")
            .setPositiveButton("그만 빤다"){dialog, id->
                finish()
            }
            .setNegativeButton("더 빤다"){dialog, id->

            }.show()
    }
}
