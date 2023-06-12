package com.example.receiptcareapp.fragment.homeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.viewModel.MainViewModel
import com.example.receiptcareapp.base.BaseFragment

//메인 프레그먼트/
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
    private val CAMERA_CODE = 98
    private val GALLERY = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val GALLERY_CODE = 101
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
                    .setTitle("종료")
                    .setMessage("꿀을 그만 빠시겠어요?")
                    .setPositiveButton("그만 빤다"){dialog, id->
                        requireActivity().finish()
                    }
                    .setNegativeButton("더 빤다"){dialog, id->

                    }.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun initData() {}

    override fun initUI() {
        with(binding){
            cameraBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_cameraFragment) }
            galleryBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_galleryFragment)}
            storageBtn.setOnClickListener{  findNavController().navigate(R.id.action_homeFragment_to_recyclerFragment)}
            settingBtn.setOnClickListener{
                val bottomDialogFragment = HomeCardBottomSheet()
                bottomDialogFragment.show(parentFragmentManager,"tag")
            }
        }
    }

    override fun initListener() {
        checkPermission(requireContext(), CAMERA)
        checkPermission(requireContext(), GALLERY)


    }

    override fun initObserver() {}

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    /*** 권한 관련 코드 ***/
    fun checkPermission(context: Context, permissions: Array<out String>): Boolean {
        Log.e("TAG", "checkPermission: 실행", )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED && permissions.contentEquals(GALLERY)) {
                    Log.e("TAG", "checkPermission: 갤러리", )
                    ActivityCompat.requestPermissions(requireActivity(), permissions, GALLERY_CODE)
                    return false
                }
                else if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED && permissions.contentEquals(CAMERA)) {
                    Log.e("TAG", "checkPermission: 카메라", )
                    ActivityCompat.requestPermissions(requireActivity(), permissions, CAMERA_CODE)
                    return false;
                }
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
                        Toast.makeText(requireActivity(), "카메라 권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            GALLERY_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireActivity(), "갤러리  권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}