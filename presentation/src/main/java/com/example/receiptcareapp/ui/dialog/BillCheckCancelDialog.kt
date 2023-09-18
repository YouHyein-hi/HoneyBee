package com.example.receiptcareapp.ui.dialog

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogDeleteBinding
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillCheckCancelDialog(
    private val viewModel:RecordShowViewModel
) : BaseDialog<DialogDeleteBinding>(DialogDeleteBinding::inflate) {

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() {
        with(binding){
            deleteCancelBtn.setOnClickListener{ dismiss() }
            deleteOkBtn.setOnClickListener{
                viewModel.billCheckCancel()
                dismiss()
            }
        }
    }

    override fun initObserver() {}
}