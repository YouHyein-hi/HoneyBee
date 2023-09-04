package com.example.receiptcareapp.ui.adapter

import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.example.receiptcareapp.util.Utils
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("textValue")
    fun setTextValue(editText: EditText, value: String?) {
        Log.e("TAG", "setTextValue: $value", )
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "textValue")
    fun getTextValue(editText: EditText): String? {
        Log.e("TAG", "getTextValue: ${editText.text?.toString()}")
        return editText.text?.toString()
    }

    @BindingAdapter("app:textValueAttrChanged")
    @JvmStatic
    fun setTextValueListener(editText: EditText, listener: InverseBindingListener?) {
        Log.e("TAG", "setTextValueListener:")

        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && editText.text != null && editText.text.isNotEmpty()) {
                editText.setText(Utils.PriceFormat(editText.text.toString()))
                return@setOnEditorActionListener true
            }
            false
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                if (editText.text != null && editText.text.contains(",")) {
                    editText.setText(Utils.CommaReplaceSpace(editText.text.toString()))
                    editText.setSelection(editText.text.length)
                } else {
                    editText.setText(editText.text?.let { Utils.PriceFormat(it.toString()) })
                }
            }
        }

    }


    // 마음대로 안돌아감
/*    @JvmStatic
    @BindingAdapter(value = ["errorHint", "highlightColor"])
    fun setErrorHintAndHighlight
                (editText: EditText, errorHint: String?, highlightColor: Int?) {
        Log.e("TAG", "setErrorHintAndHighlight: 진입", )
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                Log.e("TAG", "setErrorHintAndHighlight: !hasFocus 진입", )
                if (TextUtils.isEmpty(editText.text)) {
                    Log.e("TAG", "setErrorHintAndHighlight: isEmpty if 진입", )
                    editText.hint = errorHint
                    editText.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else {
                    Log.e("TAG", "setErrorHintAndHighlight: isEmpty else 진입", )
                    editText.backgroundTintList = highlightColor?.let { ColorStateList.valueOf(it) }
                }
            }
        }
    }

    @BindingAdapter("onTextChanged")
    @JvmStatic
    fun setOnTextChangedListener(editText: EditText, onTextChanged: (() -> Unit)?) {
        editText.doAfterTextChanged {
            onTextChanged?.invoke()
        }
    }*/
}