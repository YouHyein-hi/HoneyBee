package com.example.receiptcareapp.ui.dialog

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.example.domain.model.UpdateData
import com.example.domain.model.receive.CardData
import com.example.domain.model.receive.CardSpinnerData
import com.example.domain.model.receive.DateData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
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
    private var settingYear = 0
    private var settingMonth = 0
    private var settingDay = 0
    private lateinit var dateData : DateData
    private var newDate = listOf<String>()
    private var cardDataList: MutableList<CardSpinnerData> = mutableListOf()

    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            viewModelData = activityViewModel.selectedData.value!!
            newDate = viewModel.dateReplace(viewModelData.date)
            Log.e("TAG", "initData myData : $viewModelData")
        } else {
            showShortToast("데이터가 없습니다!")
            Log.e("TAG", "initData: 데이터가 없다")
            dismiss()
        }

        viewModel.getServerCardData()
    }

    override fun initUI() {
        getSpinner()

        // 수정 전 로컬 데이터 화면에 띄우기
        // Spinner은 아직 설정 안함
        binding.changeCardspinner
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
            Log.e("TAG", "onCreateDialog: ${dateData.year}, ${dateData.month}, ${dateData.day}")

            val myLocalDateTime = viewModel.myLocalDateTimeFuntion(dateData.year, dateData.month, dateData.day)

            Log.e("TAG", "onCreateView: ${viewModelData.uid}")
            Log.e(
                "TAG",
                "onCreateDialog: ${myLocalDateTime}, ${binding.changeBtnPrice.text}, ${cardName}, ${binding.changeBtnStore.text}, ${viewModelData.file}",
            )

            when {
                cardName == "" -> {
                    showShortToast("카드를 입력하세요.")
                }
                binding.changeBtnStore.text!!.isEmpty() -> {
                    showShortToast("가게 이름을 입력하세요.")
                }
                binding.changeBtnPrice.text!!.isEmpty() -> {
                    showShortToast("금액을 입력하세요.")
                }
                myLocalDateTime.toString() == "" -> {
                    showShortToast("날짜를 입력하세요.")
                }
                else -> {
                    Log.e("TAG", "initListener myData: $viewModelData")
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
                            sendData = AppSendData(
                                billSubmitTime = myLocalDateTime.toString(),
                                amount = binding.changeBtnPrice.text.toString(),
                                cardName = cardName,
                                storeName = binding.changeBtnStore.text.toString(),
//                                picture = activityViewModel.bitmapToUri(requireActivity(),activityViewModel.picture.value)
                                picture = viewModelData.file!!
                            )
                        )
                    }
                    dismiss()
                }
            }
        }
        binding.changeBtnNegative.setOnClickListener { dismiss() }

        binding.changeCardspinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            Log.e("TAG", "btnPrice.setOnEditorActionListener", )
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
            it?.body?.forEach { cardDataList.add(it) }
            val cardArrayList = ArrayList<CardSpinnerData>(cardDataList)
            Log.e("TAG", "cardList.observe : ${cardDataList}", )
            binding.changeCardspinner.adapter = SpinnerAdapter(requireContext(), cardArrayList)
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun getSpinner() {


    }
}