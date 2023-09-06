package com.example.receiptcareapp.ui.dialog

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.domain.model.remote.send.card.SendCardData
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
    private val homeViewModel: CardViewModel
) : BaseDialog<DialogCardAddBinding>(DialogCardAddBinding::inflate) {

    private val viewModel : CardAddViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
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
                else if(cardAddBillCheckEdit.text.toString() == ""){
                    showShortToast(getString(R.string.dialog_cardAdd_billCheckDate))
                }
                else if (cardAddBillCheckEdit.text.toString().toInt() > 31){
                    showShortToast(getString(R.string.dialog_cardAdd_billCheckDate_err2))
                }
                else{
                    if (price.contains(","))
                        price = price.replace(",", "")
                    homeViewModel.insertServerCardData(SendCardData(cardAddNameEdit.text.toString(), price.toInt(), cardAddBillCheckEdit.text.toString()))
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
                    homeViewModel.getServerCardData()
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