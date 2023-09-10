package com.example.receiptcareapp.ui.botteomSheet

import android.app.Dialog
import android.os.Bundle
import com.example.domain.model.ui.bill.CheckBillData
import com.example.domain.model.ui.bill.UiBillData
import com.example.domain.util.StringUtil
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
    private val data: CheckBillData
) : BaseBottomSheet<BottomsheetSendBinding>(
    BottomsheetSendBinding::inflate,
    "BottomSheetSendBinding"
) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {}

    override fun initUI() {
        binding.data = CheckBillData(data.cardName, data.storeAmount,data.cardAmount, data.storeName, StringUtil.changeDate(data.date),data.picture)
    }

    override fun initListener() {
        binding.sendCancleBtn.setOnClickListener { dismiss() }

        binding.sendOkBtn.setOnClickListener {
            viewModel.insertBillData(
                UiBillData(
                    date = data.date,
                    storeAmount = data.storeAmount,
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