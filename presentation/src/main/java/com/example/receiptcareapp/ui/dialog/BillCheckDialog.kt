package com.example.receiptcareapp.ui.dialog

import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogBillCheckBinding
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillCheckDialog(
    private val viewModel:RecordShowViewModel,
    private val id: String
) : BaseDialog<DialogBillCheckBinding>(DialogBillCheckBinding::inflate) {

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() {
        with(binding){
            billCheckOkBtn.setOnClickListener{
                viewModel.BillCheckData(id.toLong())
                dismiss()
            }

            billCheckCancelBtn.setOnClickListener{
                dismiss()
            }
        }
    }

    override fun initObserver() {
        viewModel.fetchState.observe(this) {
            when (it.second) {
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> dismiss()
                else -> {}
            }
            showShortToast(FetchStateHandler(it))
        }
    }

}