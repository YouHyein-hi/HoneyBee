package com.example.receiptcareapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentCameraBinding
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.fragment.homeFragment.HomeFragment
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {
    private val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
    private var photoURI : Uri? = null
    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val homeFragment : HomeFragment = HomeFragment()

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
        if (homeFragment.checkPermission(requireContext(), CAMERA)) {  // 카메라 권한 있을 시 카메라 실행함
            Log.e("TAG", "카메라 권한 있음", )
            dispatchTakePictureIntentEx()
        }
    }

    private fun dispatchTakePictureIntentEx() {
        Log.e("TAG", "dispatchTakePictureIntentEx: 진입", )
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        //카메라 불러오기
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //해당 경로에 사진이 저장될거임
        val uri : Uri? = createImageUri("JPEG_${timeStamp}_", "image/jpeg")
        Log.e("TAG", "dispatchTakePictureIntentEx: my uri : $uri", )

        photoURI = uri
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        activityResult.launch(takePictureIntent)
    }

    fun createImageUri(filename:String, mimeType:String):Uri? {
        Log.e("TAG", "createImageUri: 진입", )
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return getActivity()?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }


    //카메라 촬영 후 확인 시
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            Log.e("TAG", "onActivityResult: if 진입", )
            if(photoURI != null) {
                fragmentViewModel.takeImage(photoURI!!)
//                fragmentViewModel.getMultiPartPicture(body)
                photoURI = null
                NavHostFragment.findNavController(this).navigate(R.id.action_cameraFragment_to_showFragment)
            }
        }
        else {
            Log.e("TAG", "RESULT_OK if: else 진입", )
            findNavController().navigate(R.id.action_cameraFragment_to_homeFragment)
        }
    }

/*    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)
        return result!!
    }*/

    override fun initUI() {
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}
