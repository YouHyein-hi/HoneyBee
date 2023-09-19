package com.example.receiptcareapp.ui.dialog

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.model.remote.receive.card.CardData
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogDeleteBinding
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.receiptcareapp.databinding.DialogCardDeleteBinding
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.ui.botteomSheet.CardDetailBottomSheet
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.CardDeleteViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
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
            // 이걸 영수증이 아니라 카드로 바꿔야됨!
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
                Log.e("TAG", "initListener idid: $id", )
                cardDetailBottomSheet.dismiss() // CardDetailBottomSheet를 먼저 숨깁니다.
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
        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

}