package com.example.receiptcareapp.viewModel.dialogViewModel

import android.util.Log
import androidx.lifecycle.ViewModel

class CardBottomSheetViewModel : ViewModel(){

    init { Log.e("TAG", "HomeCardBottomSheetViewModel", ) }

    fun CommaReplaceSpace(text : String): String {
        return text.replace(",", "")
    }
}