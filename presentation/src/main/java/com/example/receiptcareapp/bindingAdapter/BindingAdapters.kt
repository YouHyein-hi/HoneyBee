package com.example.receiptcareapp.bindingAdapter

import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import android.graphics.Color
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.domain.util.StringUtil

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("textValue")
    fun setTextValue(editText: EditText, value: String?) {
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "textValue")
    fun getTextValue(editText: EditText): String? {
        return editText.text?.toString()
    }

    @BindingAdapter("app:textValueAttrChanged")
    @JvmStatic
    fun setTextValueListener(editText: EditText, listener: InverseBindingListener?) {
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && editText.text != null && editText.text.isNotEmpty()) {
                editText.setText(StringUtil.PriceFormat(editText.text.toString()))
                return@setOnEditorActionListener true
            }
            false
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                if (editText.text != null && editText.text.contains(",")) {
                    editText.setText(StringUtil.CommaReplaceSpace(editText.text.toString()))
                    editText.setSelection(editText.text.length)
                } else {
                    editText.setText(editText.text?.let { StringUtil.PriceFormat(it.toString()) })
                }
            }
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["originalHint","errorHint", "highlightColor"])
    fun setErrorHintAndHighlight(editText: EditText, originalHint : String?, errorHint: String?, highlightColor: Int) {
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus && editText.text.isEmpty()) {
                editText.hint = errorHint
                editText.backgroundTintList = ColorStateList.valueOf(Color.RED)
            } else if(hasFocus && !editText.text.isEmpty())  {
                editText.hint = originalHint // 초기 hint로 되돌리기
                editText.backgroundTintList = ColorStateList.valueOf(highlightColor)
            }
            else if(!hasFocus && !editText.text.isEmpty()){
                editText.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("editTextClickUnderBarColor")
    fun setEditTextClickUnderBarColor(editText: EditText, text: String) {
        editText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) { editText.backgroundTintList = ColorStateList.valueOf(Color.WHITE) }
            else{ editText.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT) }
        }
    }

}