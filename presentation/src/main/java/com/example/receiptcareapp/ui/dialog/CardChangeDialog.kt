package com.example.receiptcareapp.ui.dialog

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.domain.model.remote.receive.card.CardData
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.model.ui.dateTime.DateData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardAddBinding
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardChangeDialog(
    private val cardViewModel: CardViewModel,
    private val cardData:CardData
) : BaseDialog<DialogCardAddBinding>(DialogCardAddBinding::inflate) {

    private val viewModel : CardAddViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
        Log.e("TAG", "initUI: ${cardData}", )
    }

    override fun initListener() {
        with(binding){
            cardAddCancelBtn.setOnClickListener{
                dismiss()
            }
            cardAddOkBtn.setOnClickListener{
                // 업데이트 관련
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