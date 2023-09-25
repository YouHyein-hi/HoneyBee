package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.content.Intent
import android.provider.MediaStore
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel(){

    fun callGallery(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        return intent
    }
}