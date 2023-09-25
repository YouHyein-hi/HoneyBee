package com.example.receiptcareapp.ui.dialog

import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogExitBinding

class ExitDialog : BaseDialog<DialogExitBinding>(DialogExitBinding::inflate) {
    override fun initData() {}

    override fun initUI() {}

    override fun initListener() {
        binding.exitCancelBtn.setOnClickListener{
            dismiss()
        }
        binding.exitOkBtn.setOnClickListener{
            requireActivity().finish()
        }
    }

    override fun initObserver() {}

}