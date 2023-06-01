package com.example.receiptcareapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentGalleryBinding
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.base.BaseFragment

class GalleryFragment : BaseFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {

//    private val binding by lazy { FragmentGalleryBinding.inflate(layoutInflater) }
    private val GALLERY = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val GALLERY_CODE = 101

    private val viewModel : FragmentViewModel by viewModels({requireActivity()})


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("TAG", "onCreate: GalleryFragment", )
        CallGallery()
    }

    /** 갤러리 관련 코드 **/
    /* 갤러리 호출 */
    fun CallGallery() {
        Log.e("TAG", "CallGallery 실행", )

        if(checkPermission(GALLERY)){
            Log.e("TAG", "파일 권한 있음", )

            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            activityResult.launch(intent)
        }
    }
    /* 갤러리 사진 관련 함수 */
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            Log.e("TAG", "onActivityResult: if 진입", )
            val imageUri: Uri? = it.data?.data
            Log.e("TAG", "my URI: $imageUri", )
            if (imageUri != null) {
                Log.e("TAG", "data 있음", )
                viewModel.takeImage(imageUri)
                NavHostFragment.findNavController(this).navigate(R.id.action_galleryFragment_to_showFragment)
            }
            else{
                Log.e("TAG", "data 없음", )
            }
        }
        else{
            Log.e("TAG", "RESULT_OK if: else 진입", )
            findNavController().navigate(R.id.action_galleryFragment_to_homeFragment)
        }
    }


    /*** 권한 관련 코드 ***/
    fun checkPermission(permissions : Array<out String>) : Boolean{         // 실제 권한을 확인하는 곳
        Log.e("TAG", "checkPermission 실행", )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), permissions, GALLERY_CODE)
                    return false;
                }
            }
        }
        return true
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {  // 권한 확인 직후 바로 호출됨
        Log.e("TAG", "onRequestPermissionsResult 실행", )

        when(requestCode) {
            GALLERY_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "파일 권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun initUI() {
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}