package com.example.receiptcareapp.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.AppSendCardData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.ui.adapter.ShowPictureAdapter
import com.example.receiptcareapp.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ShowPictureFragment :
    BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
    private val viewModel: FragmentViewModel by viewModels({requireActivity()})
    private val activityViewModel: MainViewModel by activityViewModels()
    private var checked = ""
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0
    private var cardArray: MutableMap<String, Int>? = mutableMapOf()
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
                .load(viewModel.image.value!!)
                .into(pictureView)

            pictureView.clipToOutline = true
            val dateNow = LocalDate.now()
            val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            btnDate.text = "${dateNow.format(formatterDate)}"
            myYear = dateNow.year
            myMonth = dateNow.monthValue
            myDay = dateNow.dayOfMonth
        }
        /** Spinner 호출 **/
        getSpinner()
    }

    override fun initListener() {
        with(binding){
            /** Date Button -> DatePickerDialog 생성 **/
            btnDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    var myMonthSt : String
                    var mydaySt : String
                    myYear = year
                    myMonth = month
                    myDay = day

                    if(month < 10)
                        myMonthSt = "0${month + 1}"
                    else myMonthSt = "${month + 1}"
                    Log.e("TAG", "onViewCreated: month else~")
                    myMonth = month + 1
                    if(day < 10)
                        mydaySt = "0${day}"
                    else mydaySt = "${day}"

                    btnDate.text = "${myYear}/${myMonthSt}/${mydaySt}"
                }
                val dataDialog = DatePickerDialog(requireContext(), data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                )
                dataDialog.show()
                dataDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(Color.RED)
                dataDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            }

            /** Card 추가 Button -> Card 추가 Dialog 생성 **/
            cardaddBtn.setOnClickListener{
                cardAddDialog()
            }

            /** 금액 EidtText , 추가 **/
            btnPrice.setOnClickListener {
                if (btnPrice.text.contains(",")) {
                    btnPrice.setText(btnPrice.text.toString().replace(",", ""))
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
                    Toast.makeText(requireContext(), "카드를 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (btnStore.text!!.isEmpty()) {
                    Toast.makeText(requireContext(), "가게 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (btnDate.text.isEmpty()) {
                    Toast.makeText(requireContext(), "날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (btnPrice.text.isEmpty()) {
                    Toast.makeText(requireContext(), "금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (viewModel.image.value == null) {
                    Toast.makeText(requireContext(), "사진이 비었습니다.\n초기화면으로 돌아갑니다.", Toast.LENGTH_SHORT)
                        .show()
                    NavHostFragment.findNavController(this@ShowPictureFragment)
                        .navigate(R.id.action_showFragment_to_homeFragment)
                } else {
                    Log.e("TAG", "onViewCreated: ${myYear}, ${myMonth}, ${myDay}")
                    val myLocalDateTime = LocalDateTime.of(
                        myYear,
                        myMonth,
                        myDay,
                        LocalDateTime.now().hour,
                        LocalDateTime.now().minute,
                        LocalDateTime.now().second
                    )
                    activityViewModel.sendData(
                        AppSendData(
                            date = myLocalDateTime.toString(), amount = btnPrice.text.toString(), cardName = checked, picture = viewModel.image.value!!, storeName = binding.btnStore.text.toString())
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
                        Toast.makeText(requireContext(), "전송 완료!", Toast.LENGTH_SHORT).show()
                        NavHostFragment.findNavController(this@ShowPictureFragment).navigate(R.id.action_showFragment_to_homeFragment)
                    }
                    ConnectedState.CARD_CONNECTING_SUCCESS -> {
                        waitingView.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "카드 추가 완료!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        waitingView.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    /** Card 추가 Dialog **/
    fun cardAddDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_card, null)
        val editText_cardName = dialogView.findViewById<EditText>(R.id.dialog_cardname)
        val editText_cardPrice = dialogView.findViewById<EditText>(R.id.dialog_cardprice)

        editText_cardPrice.setOnClickListener {
            if (editText_cardPrice.text.contains(",")) {
                editText_cardPrice.setText(editText_cardPrice.text.toString().replace(",", ""))
                editText_cardPrice.setSelection(editText_cardPrice.text.length)
            }
        }
        editText_cardPrice.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE && editText_cardPrice.text.isNotEmpty()) {
                val gap = DecimalFormat("#,###")
                editText_cardPrice.setText(gap.format(editText_cardPrice.text.toString().toInt()))
            }
            handled
        }

        // EditText 비어있을 시 나타나는 style 이벤트
        val hintCardNmae = editText_cardName.hint
        val emphasis_yellow = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.emphasis_yellow))
        editText_cardName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus && editText_cardName.text.isEmpty()) {
                editText_cardName.hint = "카드 이름을 꼭 적어주세요!"
                editText_cardName.backgroundTintList = ColorStateList.valueOf(Color.RED)
            } else if(hasFocus && !editText_cardName.text.isEmpty())  {
                editText_cardName.hint = hintCardNmae // 초기 hint로 되돌리기
                editText_cardName.backgroundTintList = emphasis_yellow
            }
            else if(!hasFocus && !editText_cardName.text.isEmpty()){
                editText_cardName.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            }
        }
        val hintCardPrice = editText_cardPrice.hint
        editText_cardPrice.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus && editText_cardPrice.text.isEmpty()) {
                // 포커스를 가지고 있지 않은 경우 AND Empty인 경우
                editText_cardPrice.hint = "초기 금액을 꼭 적어주세요!"
                editText_cardPrice.backgroundTintList = ColorStateList.valueOf(Color.RED)
            } else if(hasFocus && !editText_cardPrice.text.isEmpty()) {
                editText_cardPrice.hint = hintCardPrice // 초기 hint로 되돌리기
                editText_cardPrice.backgroundTintList = emphasis_yellow
            }
            else if(!hasFocus && !editText_cardPrice.text.isEmpty()){
                editText_cardPrice.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            }
        }

        val cardAddDialog = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
            .setTitle("카드 추가")
            .setMessage("추가할 카드 이름과 초기 금액을 입력해주세요.")
            .setView(dialogView)
            .setPositiveButton("확인") { dialog, id ->
                if(editText_cardName.text.toString() == ""){
                    Log.e("TAG", "onViewCreated: 카드 이름을 입력해주세요")
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "카드 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                else if(editText_cardPrice.text.toString() == ""){
                    Log.e("TAG", "onViewCreated: 초기 금액을 입력해주세요")
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "초기 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.e("TAG", "cardAddDialog: ${editText_cardName.text}")
                    newCard = 1
                    activityViewModel.changeConnectedState(ConnectedState.CONNECTING)
                    activityViewModel.sendCardData(AppSendCardData(editText_cardName.text.toString(), editText_cardPrice.text.toString().toInt()))
                    getSpinner()
                    dialog.dismiss()
                }

            }
            .setNegativeButton("취소"){dialog, id->
                Log.e("TAG", "getSpinner: 카드 추가 취소")
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        cardAddDialog.show()
        cardAddDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(Color.RED)
        cardAddDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.BLACK)
    }

    /** Spinner 관련 **/
    private fun getSpinner() {
        activityViewModel.receiveServerCardData()
        Log.e("TAG", "getSpinner: getSpinner")
        cardArray?.let { viewModel.takeCardData(it) }
        var adapter = ShowPictureAdapter(requireContext(), arrayListOf())
        binding.spinner.adapter = adapter
        Log.e("TAG", "getSpinner: 현재 들어가있는값 : ${arrayCardList}")
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
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
}