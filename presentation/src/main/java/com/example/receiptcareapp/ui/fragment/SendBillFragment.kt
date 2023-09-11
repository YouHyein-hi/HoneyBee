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
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.remote.receive.card.CardSpinnerData
import com.example.domain.model.ui.bill.CheckBillData
import com.example.domain.model.ui.dateTime.DateData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentSendBillBinding
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
import com.example.receiptcareapp.ui.adapter.StoreSpinnerAdapter
import com.example.receiptcareapp.ui.botteomSheet.SendCheckBottomSheet
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.SendBillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class SendBillFragment :
    BaseFragment<FragmentSendBillBinding>(FragmentSendBillBinding::inflate, "ShowPictureFragment") {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: SendBillViewModel by viewModels()
    private var cardDataList: MutableList<CardSpinnerData> = mutableListOf()
    private var cardName = ""
    private var cardAmount = ""
    private var todayDate: LocalDate? = null
    private var selectedDate: LocalDate? = null
    private lateinit var dateData: DateData
    private lateinit var callback: OnBackPressedCallback
    private var storeArray = arrayListOf<String>()


    override fun initData() {
        todayDate = StringUtil.dateNow()
        selectedDate = StringUtil.dateNow()
        viewModel.getServerStoreData()
        dateData = DateData(
            year = todayDate!!.year,
            month = todayDate!!.monthValue,
            day = todayDate!!.dayOfMonth
        )

        viewModel.getServerCardData()
    }

    override fun initUI() {
        with(binding) {
            //글라이드
            Glide.with(pictureView)
                .load(activityViewModel.image.value!!)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(pictureView)
            val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            date = StringUtil.dateNow().format(formatterDate)
        }

    }

    override fun initListener() {
        with(binding) {
            /** Date Button -> DatePickerDialog 생성 **/
            sendBillDateBtn.setOnClickListener {
                val cal = Calendar.getInstance()
                val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    dateData = DateData(
                        year = year,
                        month = month + 1,
                        day = day
                    )
                    date = "${year}/${StringUtil.datePickerMonth(month)}/${StringUtil.datePickerDay(day)}"
                    selectedDate = LocalDate.of(year, month + 1, day)
                }
                val dataDialog = DatePickerDialog(
                    requireContext(), data,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                )
                dataDialog.show()
                dataDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(Color.RED)
                dataDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            }

            //TODO Binding어뎁터에서 토스트 쓸수있다면 넘기는걸로
            sendBillStoreEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int,after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length > 15) { showShortToast("15자 이내로 입력해주세요.") }
                }
            })
            sendBillPriceEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length > 10) { showShortToast("10자 이내로 입력해주세요.") }
                }
            })

            /** 완료 Button **/
            sendBillOkBtn.setOnClickListener {
                Log.e("TAG", "onViewCreated: iinin")
                var price = sendBillPriceEdit.text.toString()
                var priceZero = price.count { it == '0' }
                when {
                    cardName == "" -> { showShortToast("카드를 입력하세요.") }
                    sendBillStoreEdit.text!!.isEmpty() -> { showShortToast("가게 이름을 입력하세요.") }
                    sendBillPriceEdit.text.isEmpty() -> { showShortToast("금액을 입력하세요.") }
                    priceZero == price.length -> { showShortToast("금액에 0원은 입력이 안됩니다.")}
                    sendBillDateBtn.text.isEmpty() -> { showShortToast("날짜를 입력하세요.") }
                    selectedDate!!.isAfter(todayDate) -> { showShortToast("오늘보다 미래 날짜는 불가능합니다.") }
                    activityViewModel.image.value == null -> {
                        showShortToast("사진이 비었습니다.\n초기화면으로 돌아갑니다.")
                        NavHostFragment.findNavController(this@SendBillFragment).navigate(R.id.action_sendBillFragment_to_homeFragment)
                    }
                    else -> {
//                        if (!StringUtil.amountCheck(sendBillPriceEdit.text.toString(), cardAmount)) {
//                            showShortToast("보유금액보다 많은 비용입니다.")
//                            return@setOnClickListener
//                        }
                        val myLocalDateTime = StringUtil.myLocalDateTimeFuntion(dateData.year, dateData.month, dateData.day)
                        SendCheckBottomSheet(
                            viewModel,
                            CheckBillData(
                                cardName = cardName,
                                storeAmount = sendBillPriceEdit.text.toString(),
                                cardAmount = cardAmount,
                                storeName = binding.sendBillStoreEdit.text.toString(),
                                date = myLocalDateTime.toString(),
                                picture = activityViewModel.image.value!!
                            )
                        ).show(parentFragmentManager, "tag")
                    }

                }
            }

            sendBillCancleBtn.setOnClickListener {
                findNavController().navigate(R.id.action_sendBillFragment_to_homeFragment)
            }
        }

        binding.sendBillCardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCardData = cardDataList[position]
                cardName = selectedCardData.name
                cardAmount = selectedCardData.amount
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        // 키보드 오르락 내리락 감지
        //TODO 데이터바인딩으로 빼기
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            try {
                val layoutParams = binding.bottomLayout.layoutParams as ViewGroup.MarginLayoutParams
                val rect = Rect()
                binding.root.getWindowVisibleDisplayFrame(rect)
                val screenHeight = binding.root.height
                val keypadHeight = screenHeight - rect.bottom

                //키보드 올라옴
                if (keypadHeight > screenHeight * 0.15) {
                    layoutParams.bottomMargin = 700
                    binding.bottomLayout.layoutParams = layoutParams

                    //키보드 내려옴
                } else {
                    layoutParams.bottomMargin = 50
                    binding.bottomLayout.layoutParams = layoutParams
                }
            } catch (e: Exception) {
            }
        }

    }

    override fun initObserver() {
        //TODO 데이터바인딩
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.layoutLoadingProgress.root.isVisible = it
        }

        viewModel.response.observe(viewLifecycleOwner) {
            when (it?.status) {
                "200" -> {
                    viewModel.insertRoomData(it.uid.toString())
                    findNavController().navigate(R.id.action_sendBillFragment_to_homeFragment)
                    showShortToast("전송 성공")
                }
                else -> {
                    showShortToast("전송 실패")
                }
            }
        }


        viewModel.cardList.observe(viewLifecycleOwner) {
            if(it?.body?.isEmpty() == true){
                showShortToast("카드 데이터를 추가해주세요!")
                findNavController().navigate(R.id.action_sendBillFragment_to_homeFragment)
            }
            it?.body?.forEach { cardDataList.add(it) }
            binding.sendBillCardSpinner.adapter =
                SpinnerAdapter(requireContext(), ArrayList<CardSpinnerData>(cardDataList))
        }

        viewModel.storeList.observe(viewLifecycleOwner) { response ->
            if (!response?.body.isNullOrEmpty()) {
                storeArray.clear()
                response?.body?.map { storeArray.add(it) }
                binding.sendBillStoreEdit.setAdapter(StoreSpinnerAdapter(requireContext(), storeArray))
            }
        }

        // TODO 통신 실패시도 꺼지게 해야함 에러 예외처리 추가
        viewModel.fetchState.observe(this) {
            when (it.second) {
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> findNavController().popBackStack()
                FetchState.PARSE_ERROR -> findNavController().popBackStack()
            }
            showShortToast(FetchStateHandler(it))
        }
    }

    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_sendBillFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}