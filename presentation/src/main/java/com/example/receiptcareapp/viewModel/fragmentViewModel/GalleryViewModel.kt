package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel(){

    init { Log.e("TAG", "GalleryViewModel", ) }

    fun CallGallery(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        return intent
    }
}