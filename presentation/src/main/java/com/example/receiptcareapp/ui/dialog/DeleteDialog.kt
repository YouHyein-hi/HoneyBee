package com.example.receiptcareapp.ui.dialog

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogDeleteBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialog : BaseDialog<DialogDeleteBinding>(DialogDeleteBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel : RecordShowViewModel by viewModels()
    private lateinit var viewModelData: RecyclerData
    private lateinit var myData: RecyclerData

    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            viewModelData = activityViewModel.selectedData.value!!
            Log.e("TAG", "initData: ${viewModelData}", )
        } else {
            Log.e("TAG", "initData: 데이터가 없습니다", )
            showShortToast("데이터가 없습니다!")
            dismiss()
        }
    }

    override fun initUI() {
    }

    override fun initListener() {
        with(binding){
            changeBtnNegative.setOnClickListener{
                dismiss()
            }

            changeBtnPositive.setOnClickListener{
                Log.e("TAG", "initListener: changeBtnPositive 클릭", )
                if(viewModelData.type == ShowType.SERVER){
                    viewModel.deleteServerBillData(viewModelData.uid.toLong())
                    dismiss()
                    findNavController().popBackStack()
                }else{
                    viewModel.deleteRoomBillData(viewModelData.billSubmitTime)
                    dismiss()
                    findNavController().popBackStack()
                }
            }

        }
    }

    override fun initObserver() {
    }

}