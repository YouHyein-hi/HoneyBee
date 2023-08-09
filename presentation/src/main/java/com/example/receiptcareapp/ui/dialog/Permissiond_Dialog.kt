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

    // 인터페이스를 정의하여 이벤트를 처리하기 위한 콜백 메서드를 포함합니다.
    interface OnDismissListener {
        fun onDialogDismissed()
    }
    // 이 콜백을 호출할 변수를 선언합니다.
    private var onDismissListener: OnDismissListener? = null
    // 이벤트를 처리하기 위한 콜백을 등록하는 메서드를 정의합니다.
    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    override fun initData() {
    }

    override fun initUI() {
    }

    override fun initListener() {
        binding.permissionButton.setOnClickListener {
            // 다이얼로그가 닫힐 때 콜백 호출
            onDismissListener?.onDialogDismissed()
            dismiss()
        }
    }

    override fun initObserver() {

    }

}
