package com.example.receiptcareapp.ui.fragment

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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.ui.dialog.CardAddDialog_ShowPicture
import com.example.receiptcareapp.ui.adapter.ShowPictureAdapter
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.ShowPictureViewModel
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ShowPictureFragment :
    BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val showPictureViewModel : ShowPictureViewModel by viewModels()
    private var checked = ""
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
                .into(pictureView)

            pictureView.clipToOutline = true
            val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            btnDate.text = "${showPictureViewModel.DateNow().format(formatterDate)}"
            myYear = showPictureViewModel.DateNow().year
            myMonth = showPictureViewModel.DateNow().monthValue
            myDay = showPictureViewModel.DateNow().dayOfMonth
        }
        /** Spinner 호출 **/
        getSpinner()
    }

    override fun initListener() {
        //TODO 이 코드가 과연 괜찮은 코드일까? (CardAddDialog_ShowPicture 관련)
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
                    btnDate.text = "${myYear}/${showPictureViewModel.DatePickerMonth(month)}/${showPictureViewModel.DatePickerDay(day)}"
                }
                val dataDialog = DatePickerDialog(requireContext(), data,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dataDialog.show()
                dataDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(Color.RED)
                dataDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            }

            /** Card 추가 Button -> Card 추가 Dialog 생성 **/
            cardaddBtn.setOnClickListener{
                CardAddDialog_ShowPicture().show(parentFragmentManager, "CardAddDialog")
            }

            /** 금액 EidtText , 추가 **/
            btnPrice.setOnClickListener {
                if (btnPrice.text.contains(",")) {
                    btnPrice.setText(showPictureViewModel.CommaReplaceSpace(btnPrice.text.toString()))
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
            sendBtn.setOnClickListener {
                Log.e("TAG", "onViewCreated: iinin")
                if (checked == "") {
                    showShortToast("카드를 입력하세요.")
                } else if (btnStore.text!!.isEmpty()) {
                    showShortToast("가게 이름을 입력하세요.")
                } else if (btnDate.text.isEmpty()) {
                    showShortToast("날짜를 입력하세요.")
                } else if (btnPrice.text.isEmpty()) {
                    showShortToast("금액을 입력하세요.")
                } else if (activityViewModel.image.value == null) {
                    showShortToast("사진이 비었습니다.\n초기화면으로 돌아갑니다.")
                    NavHostFragment.findNavController(this@ShowPictureFragment)
                        .navigate(R.id.action_showFragment_to_homeFragment)
                } else {
                    Log.e("TAG", "onViewCreated: ${myYear}, ${myMonth}, ${myDay}")
                    val myLocalDateTime = showPictureViewModel.myLocalDateTimeFuntion(myYear, myMonth, myDay)
                    activityViewModel.sendData(
                        AppSendData(
                            billSubmitTime = myLocalDateTime.toString(), amount = btnPrice.text.toString(), cardName = checked, picture = activityViewModel.image.value!!, storeName = binding.btnStore.text.toString())
                    )
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
                val adapter = ShowPictureAdapter(requireContext(), myArray)
                spinner.adapter = adapter
            }

            /** 프로그래스바 컨트롤 **/
            activityViewModel.connectedState.observe(viewLifecycleOwner){
                Log.e("TAG", "onViewCreated: $it")
                when (it) {
                    ConnectedState.CONNECTING -> {
                        waitingView.visibility = View.VISIBLE
                        progressBar.visibility = View.VISIBLE
                    }
                    ConnectedState.DISCONNECTED -> {
                        waitingView.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                    }
                    ConnectedState.CONNECTING_SUCCESS -> {
                        showShortToast("전송 완료!")
                        NavHostFragment.findNavController(this@ShowPictureFragment).navigate(R.id.action_showFragment_to_homeFragment)
                    }
                    ConnectedState.CARD_CONNECTING_SUCCESS -> {
                        waitingView.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        showShortToast("카드 추가 완료!")
                    }
                    else -> {
                        waitingView.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
    /** Spinner 관련 **/
    fun getSpinner() {
        activityViewModel.receiveServerCardData()
        var adapter = ShowPictureAdapter(requireContext(), arrayListOf())
        binding.spinner.adapter = adapter
        Log.e("TAG", "getSpinner: 현재 들어가있는값 : ${arrayCardList}")
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("TAG", "onItemSelected: ${myArray[position]}")
                Log.e("TAG", "onItemSelected: ${position}")
                val spiltCard = myArray[position].split(" : ")
                checked = spiltCard[0]
                Log.e("TAG", "onItemSelected: ${checked}")
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

    //TODO 이 코드가 과연 괜찮은 코드일까? (CardAddDialog_ShowPicture 관련)
    fun setNewCardValue(value: Int) {
        newCard = value
    }
}