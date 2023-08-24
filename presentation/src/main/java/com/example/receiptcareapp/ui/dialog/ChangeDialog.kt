package com.example.receiptcareapp.ui.dialog

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.example.domain.model.UpdateData
import com.example.domain.model.receive.card.CardSpinnerData
import com.example.domain.model.receive.DateData
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.dto.LocalBillData
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
import com.example.receiptcareapp.util.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ChangeDialog(
    private val viewModel: RecordShowViewModel
) : BaseDialog<DialogChangeBinding>(DialogChangeBinding::inflate) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var viewModelData: RecyclerData
    private var cardName = ""
    private var cardId = 0
    private lateinit var dateData : DateData
    private var newDate = listOf<String>()
    private var cardDataList: MutableList<CardSpinnerData> = mutableListOf()

    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            viewModelData = activityViewModel.selectedData.value!!
            newDate = viewModel.dateReplace(viewModelData.date)
        } else {
            showShortToast("데이터가 없습니다!")
            dismiss()
        }

        viewModel.getServerCardData()
    }

    override fun initUI() {
        getSpinner()
        binding.changeBtnStore.setText(viewModelData.storeName)
        binding.changeBtnPrice.setText(viewModelData.amount)
        try {
            dateData = DateData(
                year = newDate[0].toInt(),
                month = newDate[1].toInt(),
                day = newDate[2].toInt()
            )
            binding.changeDatepicker.init(dateData.year, dateData.month - 1, dateData.day, null)
        } catch (e: NullPointerException) {
            dismiss()
            showShortToast("날짜 불러오기를 실패했습니다.")
        }

    }

    override fun initListener() {

        binding.changeBtnPositive.setOnClickListener {
            dateData = DateData(
                year = binding.changeDatepicker.year,
                month = binding.changeDatepicker.month + 1,
                day = binding.changeDatepicker.dayOfMonth
            )

            val myLocalDateTime = viewModel.myLocalDateTimeFuntion(dateData.year, dateData.month, dateData.day)

            var priceZero = binding.changeBtnPrice.text.toString().count { it == '0' }
            when {
                cardName == "" -> { showShortToast("카드를 입력하세요.") }
                binding.changeBtnStore.text!!.isEmpty() -> { showShortToast("가게 이름을 입력하세요.") }
                binding.changeBtnPrice.text!!.isEmpty() -> { showShortToast("금액을 입력하세요.") }
                priceZero == price.length -> { showShortToast("금액에 0원은 입력이 안됩니다.") }
                myLocalDateTime.toString() == "" -> { showShortToast("날짜를 입력하세요.") }
                else -> {
                    if (viewModelData.type == ShowType.SERVER) {
                        viewModel.updateServerBillData(
                            sendData = UpdateData(
                                billSubmitTime = myLocalDateTime.toString(),
                                amount = binding.changeBtnPrice.text.toString(),
                                cardName = cardName,
                                storeName = binding.changeBtnStore.text.toString()
                            ),
                            uid = viewModelData.uid,
                        )
                    } else {
                        viewModel.updateLocalBillData(
                            sendData = LocalBillData(
                                uid= viewModelData.uid,
                                billSubmitTime = myLocalDateTime.toString(),
                                amount = binding.changeBtnPrice.text.toString(),
                                cardName = cardName,
                                storeName = binding.changeBtnStore.text.toString(),
                                picture = viewModelData.file!!
                            )
                        )
                    }
                    dismiss()
                }
            }
        }
        binding.changeBtnNegative.setOnClickListener { dismiss() }

        binding.changeCardspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCardData = cardDataList[position]
                cardId = position
                cardName = selectedCardData.name
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

/*        binding.changeBtnPrice.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_NEXT && binding.changeBtnPrice.text.isNotEmpty()) {
                binding.changeBtnPrice.setText(viewModel.PriceFormat(binding.changeBtnPrice.text.toString()))
            }
            handled
        }*/

        binding.changeBtnPrice.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.changeBtnPrice.text.isNotEmpty()) {
                binding.changeBtnPrice.setText(viewModel.PriceFormat(binding.changeBtnPrice.text.toString()))
            }
            handled
        }

        binding.changeBtnPrice.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                if (binding.changeBtnPrice.text.contains(",")) {
                    binding.changeBtnPrice.setText(viewModel.CommaReplaceSpace(binding.changeBtnPrice.text.toString()))
                    binding.changeBtnPrice.setSelection(binding.changeBtnPrice.text.length)
                }
            }
            else { binding.changeBtnPrice.setText(viewModel.PriceFormat(binding.changeBtnPrice.text.toString())) }
        }

    }

    override fun initObserver() {

        viewModel.cardList.observe(viewLifecycleOwner){
            if(it?.body?.isEmpty()==true) dismiss()
            it?.body?.forEach { cardDataList.add(it) }
            val cardArrayList = ArrayList(cardDataList)
            binding.changeCardspinner.adapter = SpinnerAdapter(requireContext(), cardArrayList)
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            when (it.second) {
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> dismiss()
            }
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun getSpinner() {


    }
}