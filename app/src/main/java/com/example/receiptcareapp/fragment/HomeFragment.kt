package com.example.receiptcareapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.fragment.base.BaseFragment

//메인 프레그먼트/
//class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
class HomeFragment : Fragment() {

    private lateinit var callback: OnBackPressedCallback
    private val binding : FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val CAMERA = android.Manifest.permission.CAMERA
    private val CAMERA_CODE = 98
    private val ALBUM = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val ALBUM_CODE = 101
    private lateinit var callback: OnBackPressedCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()

        binding.camaraBtn.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_cameraFragment) }
        binding.galleryBtn.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_galleryFragment) }
        binding.storage.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_recyclerFragment) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    /*** 권한 관련 코드 ***/
    fun checkPermission() : Boolean{         // 실제 권한을 확인하는 곳
        Log.e("TAG", "MainActivity: checkPermission 실행", )
        val permissions: Array<String> = arrayOf(CAMERA, ALBUM)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), ALBUM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
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
            ALBUM_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireActivity(), "갤러리  권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /** Fragment 뒤로가기 **/
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

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}