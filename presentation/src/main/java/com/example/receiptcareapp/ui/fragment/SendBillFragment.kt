package com.example.receiptcareapp.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.model.BottomSheetData
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentSendBillBinding
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
import com.example.receiptcareapp.ui.botteomSheet.SendCheckBottomSheet
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.SendBillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
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
    private lateinit var callback: OnBackPressedCallback
    private var arrayCardList : MutableList<DomainReceiveCardData> = mutableListOf()
    private var myArray = arrayListOf<String>()
    private var newCard = 0

    override fun initData() {
        // 서버와 연결 상태 초기화.
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
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

            /** 완료 Button **/
            completeBtn.setOnClickListener {
                Log.e("TAG", "onViewCreated: iinin")
                when {
                    cardName == "" -> { showShortToast("카드를 입력하세요.") }
                    btnStore.text!!.isEmpty() -> { showShortToast("가게 이름을 입력하세요.") }
                    btnDate.text.isEmpty() -> { showShortToast("날짜를 입력하세요.") }
                    btnPrice.text.isEmpty() -> { showShortToast("금액을 입력하세요.") }
                    activityViewModel.image.value == null -> {
                        showShortToast("사진이 비었습니다.\n초기화면으로 돌아갑니다.")
                        NavHostFragment.findNavController(this@SendBillFragment)
                            .navigate(R.id.action_showFragment_to_homeFragment)
                    }
                    else -> {
                        val myLocalDateTime = viewModel.myLocalDateTimeFuntion(myYear, myMonth, myDay)
                        SendCheckBottomSheet(
                            BottomSheetData(
                                cardName = cardName,
                                amount = btnPrice.text.toString(),
                                cardAmount = cardAmount,
                                storeName = binding.btnStore.text.toString(),
                                date = myLocalDateTime.toString(),
                                picture = activityViewModel.image.value!!
                            )
                        ).show(parentFragmentManager,"tag")
//                        Log.e("TAG", "onViewCreated: ${myYear}, ${myMonth}, ${myDay}")
//                        activityViewModel.sendData(
//                            AppSendData(
//                                billSubmitTime = myLocalDateTime.toString(),
//                                amount = btnPrice.text.toString(),
//                                cardName = cardName,
//                                picture = activityViewModel.image.value!!,
//                                storeName = binding.btnStore.text.toString()
//                            )
//                        )
                    }

                }
            }

            /** 취소 Button **/
            cancleBtn.setOnClickListener {
                findNavController().navigate(R.id.action_showFragment_to_homeFragment)
            }
        }
    }

    // initData와 같은 거라고 생각하자
    override fun initObserver() {
        with(binding){
            /** CardData 관련 **/
            activityViewModel.cardData.observe(viewLifecycleOwner){
                if(myArray.isEmpty()){
                    it.forEach{myArray.add("${it.cardName} : ${it.cardAmount}")}
                }
                if(newCard == 1){
                    myArray.clear()
                    it.forEach{myArray.add("${it.cardName} : ${it.cardAmount}")}
                    newCard = 0
                }
                val adapter =
                    SpinnerAdapter(requireContext(), myArray)
                spinner.adapter = adapter
            }

            /** 프로그래스바 컨트롤 **/
            viewModel.loading.observe(viewLifecycleOwner){
                if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
                else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
            }

            viewModel.response.observe(viewLifecycleOwner){
                when(it){
                    ResponseState.SUCCESS -> {
                        findNavController().navigate(R.id.action_showFragment_to_homeFragment)
                        showShortToast("전송 성공")
                    }
                    else -> {}
                }
            }
        }
    }
    /** Spinner 관련 **/
    private fun getSpinner() {
        activityViewModel.getServerCardData()
        var adapter =
            SpinnerAdapter(requireContext(), arrayListOf())
        binding.spinner.adapter = adapter
        Log.e("TAG", "getSpinner: 현재 들어가있는값 : ${arrayCardList}")
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("TAG", "onItemSelected: ${myArray[position]}")
                Log.e("TAG", "onItemSelected: ${position}")
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
                Log.e("TAG", "onAttach@@@@@@@: ${activityViewModel.connectedState.value}")
                if (activityViewModel.connectedState.value == ConnectedState.CONNECTING) {
                    Log.e("TAG", "handleOnBackPressed: stop")
                    activityViewModel.serverCoroutineStop()
                } else {
                    Log.e("TAG", "handleOnBackPressed: back")
                    findNavController().navigate(R.id.action_showFragment_to_homeFragment)
                }
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