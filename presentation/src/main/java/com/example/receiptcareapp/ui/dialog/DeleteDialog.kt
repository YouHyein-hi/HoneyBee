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
class DeleteDialog(
    private val viewModel:RecordShowViewModel
) : BaseDialog<DialogDeleteBinding>(DialogDeleteBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var viewModelData: RecyclerData

    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            viewModelData = activityViewModel.selectedData.value!!
        } else {
            showShortToast("데이터가 없습니다!")
            dismiss()
        }
    }

    override fun initUI() {
    }

    override fun initListener() {
        with(binding){
            deleteCancelBtn.setOnClickListener{
                dismiss()
            }
            deleteOkBtn.setOnClickListener{
                if(viewModelData.type == ShowType.SERVER){
                    viewModel.deleteServerBillData(viewModelData.uid.toLong())
                    dismiss()
                }else{
                    viewModel.deleteRoomBillData(viewModelData.date)
                    findNavController().popBackStack()
                    dismiss()
                }
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