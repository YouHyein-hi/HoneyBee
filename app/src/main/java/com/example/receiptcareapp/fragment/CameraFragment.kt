package com.example.receiptcareapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentCameraBinding
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {
    private val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
    private val CAMERA_CODE = 98
    private var photoURI : Uri? = null
    private val viewModel : FragmentViewModel by viewModels({ requireActivity() })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CallCamera()
    }


    override fun onResume() {
        Log.e("TAG", "onResume: ", )
        super.onResume()
    }

    /** 카메라 관련 코드 **/
    /* 카메라 호출 */
    fun CallCamera() {
        Log.e("TAG", "CallCamera 실행", )
        if (checkPermission(CAMERA)) {  // 카메라 권한 있을 시 카메라 실행함
            Log.e("TAG", "카메라 권한 있음", )
            dispatchTakePictureIntentEx()
        }
    }

    fun createImageUri(filename:String, mimeType:String):Uri? {
        Log.e("TAG", "createImageUri: 진입", )
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return getActivity()?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun dispatchTakePictureIntentEx() {
        Log.e("TAG", "dispatchTakePictureIntentEx: 진입", )
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri : Uri? = createImageUri("JPEG_${timeStamp}_", "image/jpeg")
        photoURI = uri
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        activityResult.launch(takePictureIntent)
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            Log.e("TAG", "onActivityResult: if 진입", )
            if(photoURI != null) {
                Log.e("TAG", "REQUEST_CREATE_EX if 진입", )
                val bitmap = loadBitmapFromMediaStoreBy(photoURI!!)
                bitmap?.let { viewModel.takePicture(it) }
//                println("@@@@@@ bitmap : $bitmap")
//                println("@@@@@@ bitmap : ${bitmap?.javaClass}")
//                if (bitmap != null) {
//                    viewModel.myBitMap(bitmap)
//                }
                viewModel.takePage(1)
                photoURI = null
                NavHostFragment.findNavController(this).navigate(R.id.action_cameraFragment_to_showFragment)
            }
        }
        else {
            Log.e("TAG", "RESULT_OK if: else 진입", )
            findNavController().navigate(R.id.action_cameraFragment_to_homeFragment)
        }
    }

    fun loadBitmapFromMediaStoreBy(photoUri: Uri) : Bitmap?{
        var image: Bitmap? = null
        try {
            image = if(Build.VERSION.SDK_INT > 27) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(requireActivity().contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }
            else{
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, photoUri)
            }
        } catch(e: IOException) {
            e.printStackTrace()
        }
        return image
    }


    /*** 권한 관련 코드 ***/
    fun checkPermission(permissions : Array<out String>) : Boolean{         // 실제 권한을 확인하는 곳
        Log.e("TAG", "checkPermission 실행", )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), permissions, CAMERA_CODE)
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
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "카메라 권한을 승인해 주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
