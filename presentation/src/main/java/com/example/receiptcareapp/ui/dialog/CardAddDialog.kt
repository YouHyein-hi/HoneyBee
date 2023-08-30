package com.example.receiptcareapp.ui.dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.domain.model.remote.send.card.SendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardAddBinding
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.util.ResponseState
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

            cardAddPriceEdit.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_NEXT && cardAddPriceEdit.text.isNotEmpty()) {
                    cardAddPriceEdit.setText(viewModel.PriceFormat(cardAddPriceEdit.text.toString()))
                }
                handled
            }

            //TODO databinding으로 뺄수있음 / binding adapter 를 사용하면.
            val hintCardNmae = cardAddNameEdit.hint
            val emphasis_yellow = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.emphasis_yellow))
            cardAddNameEdit.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && cardAddNameEdit.text.isEmpty()) {
                    cardAddNameEdit.hint = getString(R.string.dialog_cardAdd_name_err)
                    cardAddNameEdit.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !cardAddNameEdit.text.isEmpty())  {
                    cardAddNameEdit.hint = hintCardNmae // 초기 hint로 되돌리기
                    cardAddNameEdit.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !cardAddNameEdit.text.isEmpty()){
                    cardAddNameEdit.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
            val hintCardPrice = cardAddPriceEdit.hint
            cardAddPriceEdit.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    if (cardAddPriceEdit.text.contains(",")) {
                        cardAddPriceEdit.setText(viewModel.CommaReplaceSpace(cardAddPriceEdit.text.toString()))
                        cardAddPriceEdit.setSelection(cardAddPriceEdit.text.length)
                    }
                }
                else { cardAddPriceEdit.setText(viewModel.PriceFormat(cardAddPriceEdit.text.toString())) }

                if (!hasFocus && cardAddPriceEdit.text.isEmpty()) {
                    // 포커스를 가지고 있지 않은 경우 AND Empty인 경우
                    cardAddPriceEdit.hint = getString(R.string.dialog_cardAdd_price_err)
                    cardAddPriceEdit.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !cardAddPriceEdit.text.isEmpty()) {
                    cardAddPriceEdit.hint = hintCardPrice // 초기 hint로 되돌리기
                    cardAddPriceEdit.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !cardAddPriceEdit.text.isEmpty()){
                    cardAddPriceEdit.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
            val hintBillCardCheck = cardAddBillCheckEdit.hint
            cardAddBillCheckEdit.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && cardAddBillCheckEdit.text.isEmpty()) {
                    // 포커스를 가지고 있지 않은 경우 AND Empty인 경우
                    cardAddBillCheckEdit.hint = getString(R.string.dialog_cardAdd_billCheckDate_err1)
                    cardAddBillCheckEdit.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !cardAddBillCheckEdit.text.isEmpty()) {
                    cardAddBillCheckEdit.hint = hintBillCardCheck // 초기 hint로 되돌리기
                    cardAddBillCheckEdit.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !cardAddBillCheckEdit.text.isEmpty()){
                    cardAddBillCheckEdit.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }

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