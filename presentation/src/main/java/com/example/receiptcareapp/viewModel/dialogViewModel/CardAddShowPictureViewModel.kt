package com.example.receiptcareapp.viewModel.dialogViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class CardAddShowPictureViewModel : ViewModel(){

    init { Log.e("TAG", "CardAddShowPictureViewModel", ) }

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

    fun PriceFormat(price : String): String? {
        return DecimalFormat("#,###").format(price.toInt())
    }

}