package com.example.receiptcareapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentGalleryBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.GalleryViewModel

class GalleryFragment : BaseFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate, "GalleryFragment") {

    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private val galleryViewModel : GalleryViewModel by viewModels()

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() { CallGallery() }

    override fun initObserver() {}


    /** 갤러리 관련 코드 **/
    /* 갤러리 호출 */
    fun CallGallery() {
        Log.e("TAG", "CallGallery 실행", )
        activityResult.launch(galleryViewModel.CallGallery())
    }

    /* 갤러리 사진 관련 함수 */
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            Log.e("TAG", "onActivityResult: if 진입", )
            val imageUri: Uri? = it.data?.data
            if (imageUri != null) {
                Log.e("TAG", "data 있음", )
                activityViewModel.takeImage(imageUri)
                NavHostFragment.findNavController(this).navigate(R.id.action_galleryFragment_to_showFragment)
            }
            else{ Log.e("TAG", "data 없음", ) }
        }
        else{
            Log.e("TAG", "RESULT_OK if: else 진입", )
            findNavController().navigate(R.id.action_galleryFragment_to_homeFragment)
        }
    }

}