package com.example.receiptcareapp.ui.dialog

import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogPermissionBinding

class PermissionDialog : BaseDialog<DialogPermissionBinding>(DialogPermissionBinding::inflate)  {

    interface OnDismissListener {
        fun onDialogDismissed()
    }
    private var onDismissListener: OnDismissListener? = null
    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() {
        dialog?.setCancelable(false)
        binding.permissionOkBtn.setOnClickListener {
            onDismissListener?.onDialogDismissed()
            dismiss()
        }
    }

    override fun initObserver() {}

}
