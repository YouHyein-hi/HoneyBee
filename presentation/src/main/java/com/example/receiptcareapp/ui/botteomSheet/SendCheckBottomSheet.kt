package com.example.receiptcareapp.ui.botteomSheet

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.example.domain.model.BottomSheetData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseBottomSheet
import com.example.receiptcareapp.databinding.SendBottomsheetBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 2023-07-25
 * pureum
 */
class SendCheckBottomSheet(
    private val bottomSheetData: BottomSheetData
) : BaseBottomSheet<SendBottomsheetBinding>(
    SendBottomsheetBinding::inflate,
    "SendBottomsheetBinding"
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {
    }

    override fun initUI() {
        binding.cardName = bottomSheetData.cardName
        binding.amount = bottomSheetData.amount
        binding.cardAmount = bottomSheetData.cardAmount
        binding.storeName = bottomSheetData.storeName
        binding.date = bottomSheetData.date.split("T").first()
    }

    override fun initListener() {
        binding.cancleBtn.setOnClickListener { dismiss() }

        binding.sendBtn.setOnClickListener {
            activityViewModel.insertServerBillData(
                AppSendData(
                    billSubmitTime = bottomSheetData.date,
                    amount = bottomSheetData.amount,
                    cardName = bottomSheetData.cardName,
                    picture = bottomSheetData.picture,
                    storeName = bottomSheetData.storeName
                )
            )
            dismiss()
        }
    }

    override fun initObserver() {
    }
}