package com.example.receiptcareapp.viewModel.fragmentViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class CardAddBottomViewModel : ViewModel(){

    init { Log.e("TAG", "CardAddBottomViewModel", ) }

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

    fun PriceFormat(price : String): String? {
        return DecimalFormat("#,###").format(price.toInt())
    }
}