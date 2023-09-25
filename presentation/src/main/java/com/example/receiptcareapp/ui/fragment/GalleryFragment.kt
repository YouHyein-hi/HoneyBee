package com.example.receiptcareapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
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

    override fun initListener() { callGallery() }

    override fun initObserver() {}


    private fun callGallery() {
        activityResult.launch(galleryViewModel.callGallery())
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val imageUri: Uri? = it.data?.data
            if (imageUri != null) {
                activityViewModel.takeImage(imageUri)
                NavHostFragment.findNavController(this).navigate(R.id.action_galleryFragment_to_showFragment)
            }
        }
        else{
            findNavController().navigate(R.id.action_galleryFragment_to_homeFragment)
        }
    }

}