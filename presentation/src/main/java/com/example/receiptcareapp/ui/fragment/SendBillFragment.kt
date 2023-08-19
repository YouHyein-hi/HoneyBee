package com.example.receiptcareapp.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.model.BottomSheetData
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentSendBillBinding
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
import com.example.receiptcareapp.ui.botteomSheet.SendCheckBottomSheet
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.SendBillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class SendBillFragment : BaseFragment<FragmentSendBillBinding>(FragmentSendBillBinding::inflate, "ShowPictureFragment") {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel : SendBillViewModel by viewModels()
    private var cardName = ""
    private var cardAmount = ""
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0
    private var todayDate : LocalDate? = null
    private var selectedDate : LocalDate? = null
    private lateinit var callback: OnBackPressedCallback
    private var arrayCardList : MutableList<DomainReceiveCardData> = mutableListOf()
    private var myArray = arrayListOf<String>()
    private var newCard = 0

    override fun initData() {
        todayDate = viewModel.dateNow()
        selectedDate = viewModel.dateNow()
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
            myYear = viewModel.dateNow().year
            myMonth = viewModel.dateNow().monthValue
            myDay = viewModel.dateNow().dayOfMonth
        }
        /** Spinner 호출 **/
        getSpinner()
    }

    override fun initListener() {

        //TODO 여기 왜있지?
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().add(this, "showPictureFragment").commit()

        with(binding){
            /** Date Button -> DatePickerDialog 생성 **/
            btnDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    myYear = year
                    myMonth = month + 1
                    myDay = day
                    btnDate.text = "${myYear}/${viewModel.datePickerMonth(month)}/${viewModel.datePickerDay(day)}"

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
            btnPrice.setOnClickListener {
                if (btnPrice.text.contains(",")) {
                    btnPrice.setText(viewModel.commaReplaceSpace(btnPrice.text.toString()))
                    btnPrice.setSelection(btnPrice.text.length)
                }
            }

            /** 금액 EidtText , 추가 **/
            btnPrice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && btnPrice.text.isNotEmpty()) {
                    val gap = DecimalFormat("#,###")
                    btnPrice.setText(gap.format(btnPrice.text.toString().replace(",","").toInt()))
                }
                handled
            }

            btnDate.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length > 15) {
                        showShortToast("15자 이내로 입력해주세요.")
                    }
                }
            })
            btnPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length > 10) {
                        showShortToast("10자 이내로 입력해주세요.")
                    }
                }
            })

            /** 완료 Button **/
            completeBtn.setOnClickListener {
                Log.e("TAG", "onViewCreated: iinin")
                when {
                    cardName == "" -> {
                        showShortToast("카드를 입력하세요.")
                    }
                    btnStore.text!!.isEmpty() -> {
                        showShortToast("가게 이름을 입력하세요.")
                    }
                    btnPrice.text.isEmpty() -> {
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
                        if(!viewModel.amountCheck(btnPrice.text.toString(), cardAmount)) {
                            showShortToast("보유금액보다 많은 비용입니다.")
                            return@setOnClickListener
                        }
                        val myLocalDateTime = viewModel.myLocalDateTimeFuntion(myYear, myMonth, myDay)
                        SendCheckBottomSheet(
                            viewModel,
                            BottomSheetData(
                                cardName = cardName,
                                amount = btnPrice.text.toString(),
                                cardAmount = cardAmount,
                                storeName = binding.btnStore.text.toString(),
                                date = myLocalDateTime.toString(),
                                picture = activityViewModel.image.value!!
                            )
                        ).show(parentFragmentManager, "tag")
                    }

                }
            }

            /** 취소 Button **/
            cancleBtn.setOnClickListener {
                findNavController().navigate(R.id.action_showFragment_to_homeFragment)
            }
        }
    }

    override fun initObserver() {
        /** 프로그래스바 컨트롤 **/
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.response.observe(viewLifecycleOwner){
            Log.e("TAG", "initObserver: com", )
            when(it){
                ResponseState.SUCCESS -> {
                    findNavController().navigate(R.id.action_showFragment_to_homeFragment)
                    showShortToast("전송 성공")
                }
                else -> {}
            }
        }

        with(binding){
            /** CardData 관련 **/
            //TODO 해당 프레그먼트에선 카드를 추가할 수 없으니,
            // 애초에 홈에서 카메라나 겔러리로 넘어가기전에 막아야할듯함
            viewModel.cardList.observe(viewLifecycleOwner){
                myArray.clear()
//                it.forEach{myArray.add("${it.cardName} : ${it.cardAmount}")}
                spinner.adapter = SpinnerAdapter(requireContext(), myArray)
            }
        }
    }
    /** Spinner 관련 **/
    private fun getSpinner() {
        viewModel.getServerCardData()
        binding.spinner.adapter = SpinnerAdapter(requireContext(), arrayListOf())
        Log.e("TAG", "getSpinner: 현재 들어가있는값 : ${arrayCardList}")
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("TAG", "onItemSelected: ${myArray[position]}")
                Log.e("TAG", "onItemSelected: ${position}")
                //TODO myArray / "카드이름 :" 50000
                // 배열 두개로 관리를 하는게 낫지않을까
                val spiltCard = myArray[position].split(" : ")
                cardName = spiltCard[0]
                cardAmount = spiltCard[1]
                Log.e("TAG", "onItemSelected: ${cardName}")
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
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

    fun setNewCardValue(value: Int) {
        newCard = value
    }
}