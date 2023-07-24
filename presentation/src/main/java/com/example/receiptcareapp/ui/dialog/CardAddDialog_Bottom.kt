package com.example.receiptcareapp.ui.dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.domain.model.send.AppSendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardBinding
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddBottomViewModel
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel

class CardAddDialog_Bottom : BaseDialog<DialogCardBinding>(DialogCardBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val cardAddBottomViewModel : CardAddBottomViewModel by viewModels()

    override fun initData() {
    }

    override fun initUI() {
        val width = resources.displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 1).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun initListener() {
        with(binding){
            dialogcardEditCardprice.setOnClickListener {
                if (dialogcardEditCardprice.text.contains(",")) {
                    dialogcardEditCardprice.setText(cardAddBottomViewModel.CommaReplaceSpace(dialogcardEditCardprice.text.toString()))
                    dialogcardEditCardprice.setSelection(dialogcardEditCardprice.text.length)
                }
            }
            dialogcardEditCardprice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && dialogcardEditCardprice.text.isNotEmpty()) {
                    dialogcardEditCardprice.setText(cardAddBottomViewModel.PriceFormat(dialogcardEditCardprice.text.toString()))
                }
                handled
            }

            val hintCardNmae = dialogcardEditCardname.hint
            val emphasis_yellow = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.emphasis_yellow))
            dialogcardEditCardname.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && dialogcardEditCardname.text.isEmpty()) {
                    dialogcardEditCardname.hint = "카드 이름을 꼭 적어주세요!"
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
                    dialogcardEditCardprice.hint = "초기 금액을 꼭 적어주세요!"
                    dialogcardEditCardprice.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !dialogcardEditCardprice.text.isEmpty()) {
                    dialogcardEditCardprice.hint = hintCardPrice // 초기 hint로 되돌리기
                    dialogcardEditCardprice.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !dialogcardEditCardprice.text.isEmpty()){
                    dialogcardEditCardprice.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }

            dialogCardBtnPositive.setOnClickListener{
                if(dialogcardEditCardname.text.toString() == ""){
                    Log.e("TAG", "onResume: 카드 이름을 입력해주세요.", )
                    dismiss()
                    showLongToast("카드 이름을 입력하세요.")
                }
                else if(dialogcardEditCardname.text.toString() == ""){
                    Log.e("TAG", "onResume: 초기 금액을 입력해주세요")
                    dismiss()
                    showLongToast("추가 금액을 입력하세요.")
                }
                else{
                    var price = cardAddBottomViewModel.CommaReplaceSpace(dialogcardEditCardprice.text.toString())
                    activityViewModel.sendCardData(AppSendCardData(dialogcardEditCardname.text.toString(), price.toInt()))
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