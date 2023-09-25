package com.example.receiptcareapp.ui.dialog

import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogPushtimeBinding
import com.example.receiptcareapp.viewModel.fragmentViewModel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PushTimeDialog(
    private val viewModel: MenuViewModel,
    private val positiveButtonClickCallback: () -> Unit
    ) : BaseDialog<DialogPushtimeBinding>(DialogPushtimeBinding::inflate) {

    override fun initData() {}

    override fun initUI() {
       try {
            val time = viewModel.getTime()
            binding.hour = time.hour ?: 0
            binding.minute = time.minute ?: 0
        } catch (e: NullPointerException) {
            dismiss()
            showShortToast("날짜 불러오기를 실패했습니다.")
        }
    }

    override fun initListener() {
        binding.pushtimeOkBtn.setOnClickListener{
            viewModel.putTime(binding.pushtimeTimeTimePicker.hour, binding.pushtimeTimeTimePicker.minute)
            positiveButtonClickCallback.invoke() // Invoke the callback
            dismiss()
        }
        binding.pushtimeCancelBtn.setOnClickListener{
            dismiss()
        }
    }

    override fun initObserver() {}

}