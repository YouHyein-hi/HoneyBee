package com.example.receiptcareapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.receiptcareapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val activityViewModel : ActivityViewModel by viewModels()

    private val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
    private val CAMERA_CODE = 98
    private val READ_EXTERNAL_STORAGE = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val WRITE_EXTERNAL_STORAGE = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val ALBUM_CODE = 101

    private val Camera = android.Manifest.permission.CAMERA
    private val Album = android.Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermission()
        //activityViewModel.sendData()
    }

    /*** 권한 관련 코드 ***/
    fun checkPermission() : Boolean{         // 실제 권한을 확인하는 곳
        Log.e("TAG", "MainActivity: checkPermission 실행", )
        val permissions: Array<String> = arrayOf(Camera, Album)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Camera) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Album) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0)
            }
        }
        return true
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {  // 권한 확인 직후 바로 호출됨
        Log.e("TAG", "MainActivity: onRequestPermissionsResult 실행", )

        when(requestCode) {
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            ALBUM_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "갤러리  권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


}
