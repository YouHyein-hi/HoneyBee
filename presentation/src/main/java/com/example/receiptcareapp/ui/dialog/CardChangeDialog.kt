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
import com.example.receiptcareapp.databinding.DialogCardChangeBinding
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardChangeDialog(
    private val cardViewModel: CardViewModel,
    private val cardData:CardData
) : BaseDialog<DialogCardChangeBinding>(DialogCardChangeBinding::inflate) {

    private val viewModel : CardAddViewModel by viewModels()
    private lateinit var dateData : DateData
    private var newDate = listOf<String>()

    override fun initData() {
        cardViewModel.textValue = cardData.cardAmount
        Log.e("TAG", "initData: ${cardData.cardExpireDate}", )
        newDate = StringUtil.dateReplaceLine(cardData.cardExpireDate.toString())

        try {
            dateData = DateData(
                year = newDate[0].toInt(),
                month = newDate[1].toInt(),
                day = newDate[2].toInt()
            )
        } catch (e: NullPointerException) {
            dismiss()
            showShortToast("날짜 불러오기를 실패했습니다.")
        }
    }

    override fun initUI() {
        Log.e("TAG", "initUI: ${cardData}", )
        binding.data = cardData
        binding.cardAddPriceEdit.setText(cardViewModel.textValue)
        binding.cardAddDateDatePicker.init(dateData.year, dateData.month - 1, dateData.day, null)
    }

    override fun initListener() {
        with(binding){
            cardAddCancelBtn.setOnClickListener{
                dismiss()
            }
            cardAddOkBtn.setOnClickListener{
                dateData = DateData(
                    year = binding.cardAddDateDatePicker.year,
                    month = binding.cardAddDateDatePicker.month + 1,
                    day = binding.cardAddDateDatePicker.dayOfMonth
                )
                val myDateTime = StringUtil.myLocalDateFuntion(dateData.year, dateData.month, dateData.day)

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