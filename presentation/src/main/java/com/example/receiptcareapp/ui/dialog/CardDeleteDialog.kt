package com.example.receiptcareapp.ui.dialog

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardDeleteBinding
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.ui.botteomSheet.CardDetailBottomSheet
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.dialogViewModel.CardDeleteViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardDeleteDialog(
    private val cardViewModel: CardViewModel,
    private val cardDetailBottomSheet: CardDetailBottomSheet
) : BaseDialog<DialogCardDeleteBinding>(DialogCardDeleteBinding::inflate) {

    private val viewModel : CardDeleteViewModel by viewModels()
    private var id : Long = 0

    override fun initData() {
        if (cardViewModel.id.value != null) {
            id = cardViewModel.id.value!!
        } else {
            showShortToast("데이터가 없습니다!")
            dismiss()
        }
    }

    override fun initUI() {}

    override fun initListener() {
        with(binding){
            deleteCancelBtn.setOnClickListener{
                dismiss()
            }
            deleteOkBtn.setOnClickListener{
                cardDetailBottomSheet.dismiss()
                cardViewModel.deleteServerCardDate(id)
                dismiss()
            }
        }
    }

    override fun initObserver() {
        viewModel.response.observe(viewLifecycleOwner){
            when(it){
                ResponseState.UPDATE_SUCCESS -> {
                    cardViewModel.getServerCardData()
                    dismiss()
                }
                else->{}
            }
        }

        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

}