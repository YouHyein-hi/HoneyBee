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
    override fun initData() {

    }

    override fun initUI() {
        try {
            binding.pushtimeTimeTimePicker.hour = viewModel.getTime().hour!!
            binding.pushtimeTimeTimePicker.minute = viewModel.getTime().minute!!
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

    override fun initObserver() {

    }


    /*
                    val timeDialog = TimePickerDialog(requireContext(), data,
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
     */
}