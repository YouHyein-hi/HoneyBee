package com.example.receiptcareapp.ui.dialog

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.domain.model.remote.send.card.SendCardData
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
class CardAddDialog(
    private val cardViewModel: CardViewModel
) : BaseDialog<DialogCardAddBinding>(DialogCardAddBinding::inflate) {

    private val viewModel : CardAddViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
        val date = StringUtil.dateNow()
        binding.cardAddDateDatePicker.init(date.year, date.monthValue-1, date.dayOfMonth, null)
    }

    override fun initListener() {
        with(binding){
            cardAddOkBtn.setOnClickListener{
                var price = cardAddPriceEdit.text.toString()
                var priceZero = price.count { it == '0' }
                var cardName = cardAddNameEdit.text.toString()
                if(cardAddNameEdit.text.toString() == ""){
                    showShortToast(getString(R.string.dialog_cardAdd_name))
                }
                else if(cardAddPriceEdit.text.toString() == ""){
                    showShortToast(getString(R.string.dialog_cardAdd_price))
                }
                else if(priceZero == price.length){
                    showShortToast(getString(R.string.dialog_cardAdd_price_zero))
                }
                else{
                    if (price.contains(","))
                        price = price.replace(",", "")
                    cardViewModel.insertServerCardData(SendCardData(cardAddNameEdit.text.toString(), price.toInt(), "2023-01-18"))
                    dismiss()
                }
            }

            cardAddCancelBtn.setOnClickListener{
                Log.e("TAG", "onResume: 카드 추가 취소", )
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