package com.example.receiptcareapp.bindingAdapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import android.graphics.Color
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 키보드를 숨깁니다.
                Log.e("TAG", "setTextValueListener: 완료 if 진입", )
                val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)

                if(editText.text != null && editText.text.isNotEmpty()){
                    editText.setText(StringUtil.priceFormat(editText.text.toString()))
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener true
            }
            false
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                if (editText.text != null && editText.text.contains(",")) {
                    editText.setText(StringUtil.commaReplaceSpace(editText.text.toString()))
                    editText.setSelection(editText.text.length)
                } else {
                    editText.setText(editText.text?.let { StringUtil.priceFormat(it.toString()) })
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