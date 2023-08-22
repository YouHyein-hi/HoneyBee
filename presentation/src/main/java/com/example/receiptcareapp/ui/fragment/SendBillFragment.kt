package com.example.receiptcareapp.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.model.BottomSheetData
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.local.DomainRoomData
import com.example.domain.model.receive.CardData
import com.example.domain.model.receive.CardSpinnerData
import com.example.domain.model.receive.DateData
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentSendBillBinding
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
import com.example.receiptcareapp.ui.adapter.StoreSpinner
import com.example.receiptcareapp.ui.botteomSheet.SendCheckBottomSheet
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.SendBillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class SendBillFragment : BaseFragment<FragmentSendBillBinding>(FragmentSendBillBinding::inflate, "ShowPictureFragment") {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel : SendBillViewModel by viewModels()
    private var cardDataList: MutableList<CardSpinnerData> = mutableListOf()
    private var cardName = ""
    private var cardAmount = ""
    private var todayDate : LocalDate? = null
    private var selectedDate : LocalDate ? = null
    private lateinit var dateData : DateData
    private lateinit var callback: OnBackPressedCallback
    private var cardArray = arrayListOf<String>()
    private var storeArray = arrayListOf<String>()


    override fun initData() {
        todayDate = viewModel.dateNow()
        selectedDate = viewModel.dateNow()
        viewModel.getServerStoreData()
        //TODO 이 부분 한번만 둘러오기
        dateData = DateData(
            year = viewModel.dateNow().year,
            month = viewModel.dateNow().monthValue,
            day = viewModel.dateNow().dayOfMonth
        )
    }

    override fun initUI() {
        with(binding){
            //글라이드
            Glide.with(pictureView)
                .load(activityViewModel.image.value!!)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(pictureView)
            val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            btnDate.text = "${viewModel.dateNow().format(formatterDate)}"
        }
        /** Spinner 호출 **/
        getSpinner()
        //생성
    }

    override fun initListener() {
        with(binding){
            /** Date Button -> DatePickerDialog 생성 **/
            btnDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    dateData = DateData(
                        year = year,
                        month = month + 1,
                        day = day
                    )
                    btnDate.text = "${year}/${viewModel.datePickerMonth(month)}/${viewModel.datePickerDay(day)}"
                    selectedDate = LocalDate.of(year, month + 1, day)
                }
                val dataDialog = DatePickerDialog(requireContext(), data,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dataDialog.show()
                dataDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(Color.RED)
                dataDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            }

            /** 금액 EidtText , 추가 **/
            editTxtPrice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && editTxtPrice.text.isNotEmpty()) {
                    editTxtPrice.setText(viewModel.PriceFormat(editTxtPrice.text.toString()))
                }
                handled
            }

            editTxtStore.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length > 15) {
                        showShortToast("15자 이내로 입력해주세요.")
                    }
                }
            })

            editTxtPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length > 10) {
                        showShortToast("10자 이내로 입력해주세요.")
                    }
                }
            })
            editTxtPrice.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    if (editTxtPrice.text.contains(",")) {
                        editTxtPrice.setText(viewModel.CommaReplaceSpace(editTxtPrice.text.toString()))
                        editTxtPrice.setSelection(editTxtPrice.text.length)
                    }
                }
                else { editTxtPrice.setText(viewModel.PriceFormat(editTxtPrice.text.toString())) }
            }

            /** 완료 Button **/
            completeBtn.setOnClickListener {
                Log.e("TAG", "onViewCreated: iinin")
                when {
                    cardName == "" -> {
                        showShortToast("카드를 입력하세요.")
                    }
                    editTxtStore.text!!.isEmpty() -> {
                        showShortToast("가게 이름을 입력하세요.")
                    }
                    editTxtPrice.text.isEmpty() -> {
                        showShortToast("금액을 입력하세요.")
                    }
                    btnDate.text.isEmpty() -> {
                        showShortToast("날짜를 입력하세요.")
                    }
                    selectedDate!!.isAfter(todayDate) -> {
                        showShortToast("오늘보다 미래 날짜는 불가능합니다.")
                        Log.e("TAG", "SendBillFragment: 오늘보다 미래 날짜는 불가능합니다.", )
                    }
                    activityViewModel.image.value == null -> {
                        showShortToast("사진이 비었습니다.\n초기화면으로 돌아갑니다.")
                        NavHostFragment.findNavController(this@SendBillFragment).navigate(R.id.action_showFragment_to_homeFragment)
                    }
                    else -> {
                        if(!viewModel.amountCheck(editTxtPrice.text.toString(), cardAmount)) {
                            showShortToast("보유금액보다 많은 비용입니다.")
                            return@setOnClickListener
                        }
                        val myLocalDateTime = viewModel.myLocalDateTimeFuntion(dateData.year, dateData.month, dateData.month)
                        SendCheckBottomSheet(
                            viewModel,
                            BottomSheetData(
                                cardName = cardName,
                                amount = editTxtPrice.text.toString(),
                                cardAmount = cardAmount,
                                storeName = binding.editTxtStore.text.toString(),
                                date = myLocalDateTime.toString(),
                                picture = activityViewModel.image.value!!
                            )
                        ).show(parentFragmentManager, "tag")
                    }

                }
            }

            cancleBtn.setOnClickListener {
                findNavController().navigate(R.id.action_showFragment_to_homeFragment)
            }
        }
    }

    override fun initObserver() {
        viewModel.loading.observe(viewLifecycleOwner){
            binding.layoutLoadingProgress.root.isVisible = it
        }

        viewModel.response.observe(viewLifecycleOwner){
            when(it?.status){
                "200" -> {
                    viewModel.insertRoomData(it.uid.toString())
                    findNavController().navigate(R.id.action_showFragment_to_homeFragment)
                    showShortToast("전송 성공")
                }
                else -> {showShortToast("전송 실패")}
            }
        }

        //TODO 비었을경우에 대처, 카드리스트가 비었을때 홈으로 등등
        viewModel.cardList.observe(viewLifecycleOwner){
            it.body?.forEach { cardDataList.add(it) }
            binding.spinner.adapter = SpinnerAdapter(requireContext(), ArrayList<CardSpinnerData>(cardDataList))
        }

        viewModel.storeList.observe(viewLifecycleOwner){response ->
            if(!response?.body.isNullOrEmpty()){
                storeArray.clear()
                response?.body?.map { storeArray.add(it) }
                binding.editTxtStore.setAdapter(StoreSpinner(requireContext(), storeArray))
            }
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }
    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_showFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
    /** Spinner 관련 **/
    //TODO setonClick리스너는 밖으로 빼기
    private fun getSpinner() {
        viewModel.getServerCardData()
        Log.e("TAG", "getSpinner: ${cardDataList}", )
        //TODO 이부분 빼도 오류없는지 보고 빼기
        binding.spinner.adapter = SpinnerAdapter(requireContext(), arrayListOf())
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCardData = cardDataList[position]
                cardName = selectedCardData.name
                cardAmount = selectedCardData.amount
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}


//            키보드 오르락 내리락 감지
//            binding.root.viewTreeObserver.addOnGlobalLayoutListener {
//                try {
//                    val layoutParams = binding.bottomLayout.layoutParams as ViewGroup.MarginLayoutParams
//                    val rect = Rect()
//                    binding.root.getWindowVisibleDisplayFrame(rect)
//                    val screenHeight = binding.root.height
//                    val keypadHeight = screenHeight - rect.bottom
//
//                    //키보드 올라옴
//                    if (keypadHeight > screenHeight * 0.15) {
//                        layoutParams.bottomMargin = 700
//                        binding.bottomLayout.layoutParams = layoutParams
//
//                    //키보드 내려옴
//                    } else {
//                        layoutParams.bottomMargin = 50
//                        binding.bottomLayout.layoutParams = layoutParams
//                    }
//                }catch (e:Exception){}
//            }