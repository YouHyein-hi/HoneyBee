package com.example.receiptcareapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentCameraBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.viewModel.fragmentViewModel.CameraViewModel
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel

class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate, "CameraFragment") {
    private var photoURI : Uri? = null
    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private val viewModel : CameraViewModel by viewModels()

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() { callCamera() }

    override fun initObserver() {}

    private fun callCamera() {
        dispatchTakePictureIntentEx()
    }

    private fun dispatchTakePictureIntentEx() {
        activityResult.launch(viewModel.dispatchTakePictureIntentExViewModel(requireActivity()))
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            photoURI = viewModel.photoUri.value
            if(photoURI != null) {
                activityViewModel.takeImage(photoURI!!)
                photoURI = null
                findNavController().navigate(R.id.action_cameraFragment_to_showFragment)
            }
        }
        else {
            findNavController().navigate(R.id.action_cameraFragment_to_homeFragment)
        }
    }
}
