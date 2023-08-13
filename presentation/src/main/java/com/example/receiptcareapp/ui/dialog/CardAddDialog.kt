package com.example.receiptcareapp.ui.dialog

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.domain.model.send.AppSendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardAddBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DecimalFormat

class CardAddDialog : BaseDialog<DialogCardAddBinding>(DialogCardAddBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val cardAddViewModel : CardAddViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
    }

    override fun initListener() {
        with(binding){
            dialogcardEditCardprice.setOnClickListener {
                if (dialogcardEditCardprice.text.contains(",")) {
                    dialogcardEditCardprice.setText(cardAddViewModel.CommaReplaceSpace(dialogcardEditCardprice.text.toString()))
                    dialogcardEditCardprice.setSelection(dialogcardEditCardprice.text.length)
                }
            }
            dialogcardEditCardprice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_NEXT && dialogcardEditCardprice.text.isNotEmpty()) {
                    val gap = DecimalFormat("#,###")
                    dialogcardEditCardprice.setText(gap.format(dialogcardEditCardprice.text.toString().replace(",","").toInt()))
//                    dialogcardEditCardprice.setText(cardAddViewModel.PriceFormat(dialogcardEditCardprice.text.toString()))
                }
                handled
            }

            val hintCardNmae = dialogcardEditCardname.hint
            val emphasis_yellow = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.emphasis_yellow))
            dialogcardEditCardname.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && dialogcardEditCardname.text.isEmpty()) {
                    dialogcardEditCardname.hint = getString(R.string.dialog_cardAdd_name_err)
                    dialogcardEditCardname.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !dialogcardEditCardname.text.isEmpty())  {
                    dialogcardEditCardname.hint = hintCardNmae // 초기 hint로 되돌리기
                    dialogcardEditCardname.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !dialogcardEditCardname.text.isEmpty()){
                    dialogcardEditCardname.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
            val hintCardPrice = dialogcardEditCardprice.hint
            dialogcardEditCardprice.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && dialogcardEditCardprice.text.isEmpty()) {
                    // 포커스를 가지고 있지 않은 경우 AND Empty인 경우
                    dialogcardEditCardprice.hint = getString(R.string.dialog_cardAdd_price_err)
                    dialogcardEditCardprice.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !dialogcardEditCardprice.text.isEmpty()) {
                    dialogcardEditCardprice.hint = hintCardPrice // 초기 hint로 되돌리기
                    dialogcardEditCardprice.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !dialogcardEditCardprice.text.isEmpty()){
                    dialogcardEditCardprice.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
            val hintBillCardCheck = dialogcardEditBillCardCheck.hint
            dialogcardEditBillCardCheck.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && dialogcardEditBillCardCheck.text.isEmpty()) {
                    // 포커스를 가지고 있지 않은 경우 AND Empty인 경우
                    dialogcardEditBillCardCheck.hint = getString(R.string.dialog_cardAdd_billCheckDate_err1)
                    dialogcardEditBillCardCheck.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !dialogcardEditBillCardCheck.text.isEmpty()) {
                    dialogcardEditBillCardCheck.hint = hintBillCardCheck // 초기 hint로 되돌리기
                    dialogcardEditBillCardCheck.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !dialogcardEditBillCardCheck.text.isEmpty()){
                    dialogcardEditBillCardCheck.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }

            dialogCardBtnPositive.setOnClickListener{
                if(dialogcardEditCardname.text.toString() == ""){
                    Log.e("TAG", "initListener: 카드 이름을 입력해주세요.", )
                    dismiss()
                    showLongToast(getString(R.string.dialog_cardAdd_name))
                }
                else if(dialogcardEditCardname.text.toString() == ""){
                    Log.e("TAG", "initListener: 초기 금액을 입력해주세요")
                    dismiss()
                    showLongToast(getString(R.string.dialog_cardAdd_price))
                }
                else if(dialogcardEditBillCardCheck.text.toString() == ""){
                    Log.e("TAG", "initListener: 청구 날짜를 입력해주세요.", )
                    dismiss()
                    showLongToast(getString(R.string.dialog_cardAdd_billCheckDate))
                }
                else if (dialogcardEditBillCardCheck.text.toString().toInt() > 31){

                    Log.e("TAG", "initListener: 청구 날짜를 입력해주세요.", )
                    dismiss()
                    showLongToast(getString(R.string.dialog_cardAdd_billCheckDate_err2))
                }
                else{  // TODO 이제 billCardCheck 추가되면 여기에 추가시켜야됨!
                    var price = dialogcardEditCardprice.text.toString()
                    if (price.contains(","))
                        price = dialogcardEditCardprice.text.toString().replace(",", "")

                    activityViewModel.insertServerCardData(AppSendCardData(dialogcardEditCardname.text.toString(), price.toInt(), dialogcardEditBillCardCheck.text.toString()))
                    dismiss()
                }
            }

            dialogCardBtnNegative.setOnClickListener{
                Log.e("TAG", "onResume: 카드 추가 취소", )
                dismiss()
            }

        }
    }

    override fun initObserver() {
    }


}