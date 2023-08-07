package com.example.receiptcareapp.ui.dialog

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.AppSendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardChangeBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.CardChangeViewModel
import java.text.DecimalFormat

class CardChangeDialog(val cardData: DomainReceiveCardData) : BaseDialog<DialogCardChangeBinding>(DialogCardChangeBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val cardChangeViewModel : CardChangeViewModel by viewModels()

    override fun initData() {

    }

    override fun initUI() {

    }

    override fun initListener() {
        with(binding){
            dialogCardChangeTextCardname.text = "${cardData.cardName} 카드"

            dialogCardChangeEditCardprice.setOnClickListener{
                if (dialogCardChangeEditCardprice.text.contains(",")) {
                    dialogCardChangeEditCardprice.setText(dialogCardChangeEditCardprice.text.toString().replace(",", ""))
                    dialogCardChangeEditCardprice.setSelection(dialogCardChangeEditCardprice.text.length)
                }
            }

            dialogCardChangeEditCardprice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && dialogCardChangeEditCardprice.text.isNotEmpty()) {
                    val gap = DecimalFormat("#,###")
                    dialogCardChangeEditCardprice.setText(gap.format(dialogCardChangeEditCardprice.text.toString().toInt()))
                }
                handled
            }

            dialogCardChangeBtnPositive.setOnClickListener{
                if(dialogCardChangeEditCardprice.text.toString() == ""){
                    Log.e("TAG", "initListener: 수정할 금액을 입력해주세요.", )
                    dismiss()
                    showShortToast(getString(R.string.dialog_cardChange_price))
                }
                else {
                    var price = cardChangeViewModel.CommaReplaceSpace(dialogCardChangeEditCardprice.text.toString())
                    activityViewModel.sendCardData(AppSendCardData(cardData.cardName, price.toInt()))
                    dismiss()
                }
            }

            dialogCardChangeBtnNegative.setOnClickListener{
                Log.e("TAG", "onResume: 카드 추가 취소", )
                dismiss()
            }

        }
    }

    override fun initObserver() {

    }


}