package com.example.receiptcareapp.ui.botteomSheet

import android.app.Dialog
import android.os.Bundle
import com.example.domain.model.BottomSheetData
import com.example.domain.model.send.AppSendData
import com.example.domain.util.changeDate
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseBottomSheet
import com.example.receiptcareapp.databinding.BottomsheetSendBinding
import com.example.receiptcareapp.viewModel.fragmentViewModel.SendBillViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 2023-07-25
 * pureum
 */
@AndroidEntryPoint
class SendCheckBottomSheet(
    private val viewModel: SendBillViewModel,
    private val data: BottomSheetData
) : BaseBottomSheet<BottomsheetSendBinding>(
    BottomsheetSendBinding::inflate,
    "BottomSheetSendBinding"
) {

    private val gap = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {

    }

    override fun initUI() {
        binding.data = BottomSheetData(data.cardName, data.amount,data.cardAmount, data.storeName, changeDate(data.date),data.picture)
    }

    override fun initListener() {
        binding.cancleBtn.setOnClickListener { dismiss() }

        binding.sendBtn.setOnClickListener {
            viewModel.insertBillData(
                AppSendData(
                    billSubmitTime = data.date,
                    amount = data.amount,
                    cardName = data.cardName,
                    picture = data.picture,
                    storeName = data.storeName
                )
            )
            dismiss()
        }
    }
    override fun initObserver() {}
}