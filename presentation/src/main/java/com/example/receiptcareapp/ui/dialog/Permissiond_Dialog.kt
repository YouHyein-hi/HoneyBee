package com.example.receiptcareapp.ui.dialog

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogPermissionBinding

class Permissiond_Dialog : BaseDialog<DialogPermissionBinding>(DialogPermissionBinding::inflate)  {

    interface OnPermissionButtonClickListener {
        fun onPermissionButtonClicked()
    }
    private var onPermissionButtonClickListener: OnPermissionButtonClickListener? = null

    fun setOnPermissionButtonClickListener(listener: OnPermissionButtonClickListener) {
        onPermissionButtonClickListener = listener
    }

    override fun initData() {
    }

    override fun initUI() {
    }

    override fun initListener() {
        binding.permissionButton.setOnClickListener {
            // 다이얼로그가 닫힐 때 콜백 호출
            onPermissionButtonClickListener?.onPermissionButtonClicked()
            dismiss()
        }
    }

    override fun initObserver() {

    }

}
