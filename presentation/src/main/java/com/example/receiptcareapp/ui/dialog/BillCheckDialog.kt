package com.example.receiptcareapp.ui.dialog

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogDeleteBinding
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.receiptcareapp.databinding.DialogBillCheckBinding
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillCheckDialog(
    private val viewModel:RecordShowViewModel,
    private val id: String
) : BaseDialog<DialogBillCheckBinding>(DialogBillCheckBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var viewModelData: RecyclerData

    override fun initData() {

    }

    override fun initUI() {
    }

    override fun initListener() {
        with(binding){
            binding.billCheckOkBtn.setOnClickListener{
                viewModel.BillCheckData(id.toLong())
                dismiss()
            }

            binding.billCheckCancelBtn.setOnClickListener{
                dismiss()
            }
        }
    }

    override fun initObserver() {
        // Err관리
        viewModel.fetchState.observe(this) {
            when (it.second) {
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> dismiss()
                else -> {}
            }
            showShortToast(FetchStateHandler(it))
        }
    }

}