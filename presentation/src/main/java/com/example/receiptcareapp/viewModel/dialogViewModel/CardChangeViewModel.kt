package com.example.receiptcareapp.viewModel.dialogViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class CardChangeViewModel : ViewModel(){

    init { Log.e("TAG", "CardChangeViewModel", ) }

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }

}