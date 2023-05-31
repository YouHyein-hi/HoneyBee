package com.example.receiptcareapp.fragment.showPictureFragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
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
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.AppSendCardData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import com.example.receiptcareapp.base.BaseFragment
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ShowPictureFragment :
    BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
    private val viewModel: FragmentViewModel by viewModels({ requireActivity() })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Spinner 호출 **/
        getSpinner()

        Log.e("TAG", "viewModel.image.value : ${viewModel.image.value}")
        binding.pictureView.setImageURI(viewModel.image.value)
        binding.pictureView.clipToOutline = true

        // 서버와 연결 상태 초기화.
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)

        val dateNow = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        binding.btnDate.text = "${dateNow.format(formatterDate)}"
        myYear = dateNow.year
        myMonth = dateNow.monthValue
        myDay = dateNow.dayOfMonth


        activityViewModel.cardData.observe(viewLifecycleOwner){
            //val myArray = arrayListOf<String>()
            if(myArray.isEmpty()){
                it.forEach{myArray.add("${it.cardName} : ${it.cardAmount}")}
            }
            if(newCard == 1){
                myArray.clear()
                it.forEach{myArray.add("${it.cardName} : ${it.cardAmount}")}
                newCard = 0
            }
            val adapter = SpinnerCustomAdapter(requireContext(), myArray)
            binding.spinner.adapter = adapter
        }

        //날짜 관리
        binding.btnDate.setOnClickListener {
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
                    Log.e("TAG", "onViewCreated: month else~", )
                    myMonth = month + 1
                if(day < 10)
                    mydaySt = "0${day}"
                else mydaySt = "${day}"

                binding.btnDate.text = "${myYear}/${myMonthSt}/${mydaySt}"
            }
            val dataDialog = DatePickerDialog(requireContext(), data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )
            dataDialog.show()
            dataDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
            dataDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLACK)
        }

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
            if(it==ConnectedState.CONNECTING) {
                binding.waitingView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }
            else if(it==ConnectedState.DISCONNECTED){
                binding.waitingView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
            else if(it==ConnectedState.CONNECTING_SUCCESS){
                Toast.makeText(requireContext(), "전송 완료!", Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            }else if(it == ConnectedState.CARD_CONNECTING_SUCCESS){
                binding.waitingView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "카드 추가 완료!", Toast.LENGTH_SHORT).show()
            } else{
                binding.waitingView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }


        binding.cardaddBtn.setOnClickListener{
            cardAddDialog()
        }

        /** 카드 삭제 관련 코드 **/
        binding.cardminusBtn.setOnClickListener(){
            cardMinusDialog()
        }

        binding.btnPrice.setOnClickListener {
            if (binding.btnPrice.text.contains(",")) {
                binding.btnPrice.setText(binding.btnPrice.text.toString().replace(",", ""))
                binding.btnPrice.setSelection(binding.btnPrice.text.length)
            }
        }

        binding.btnPrice.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.btnPrice.text.isNotEmpty()) {
                val gap = DecimalFormat("#,###")
                binding.btnPrice.setText(gap.format(binding.btnPrice.text.toString().replace(",","").toInt()))
            }
            handled
        }


        binding.sendBtn.setOnClickListener {
            Log.e("TAG", "onViewCreated: iinin")
            if (checked == "") {
                Toast.makeText(requireContext(), "카드를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnStore.text!!.isEmpty()) {
                Toast.makeText(requireContext(), "가게 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnDate.text.isEmpty()) {
                Toast.makeText(requireContext(), "날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnPrice.text.isEmpty()) {
                Toast.makeText(requireContext(), "금액을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (viewModel.image.value == null) {
                Toast.makeText(requireContext(), "사진이 비었습니다.\n초기화면으로 돌아갑니다.", Toast.LENGTH_SHORT)
                    .show()
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_showFragment_to_homeFragment)
            } else {
                Log.e("TAG", "onViewCreated: ${myYear}, ${myMonth}, ${myDay}", )
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
                        date = myLocalDateTime.toString(), amount = binding.btnPrice.text.toString(), cardName = checked, picture = viewModel.image.value!!, storeName = binding.btnStore.text.toString())
                )
            }
        }

        //취소버튼
        binding.cancleBtn.setOnClickListener {
            findNavController().navigate(R.id.action_showFragment_to_homeFragment)
        }

    }

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
                    Log.e("TAG", "onViewCreated: 카드 이름을 입력해주세요", )
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "카드 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                else if(editText_cardPrice.text.toString() == ""){
                    Log.e("TAG", "onViewCreated: 초기 금액을 입력해주세요", )
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "초기 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.e("TAG", "cardAddDialog: ${editText_cardName.text}", )
                    newCard = 1
                    activityViewModel.changeConnectedState(ConnectedState.CONNECTING)
                    activityViewModel.sendCardData(AppSendCardData(editText_cardName.text.toString(), editText_cardPrice.text.toString().toInt()))
                    getSpinner()
                    dialog.dismiss()
                }

            }
            .setNegativeButton("취소"){dialog, id->
                Log.e("TAG", "getSpinner: 카드 추가 취소", )
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

    fun cardMinusDialog(){
        val array : Map<String, Int>? = viewModel.card.value
        Log.e("TAG", "minusDialog: ${array}", )
        val ArrayCard : Array<String>

        if (array != null) {
            for(i in array){
//                arrayCardList.add("${i.key} : ${i.value}")
                Log.e("TAG", "minusDialog: ${arrayCardList}", )
            }
        }

        ArrayCard = myArray.toTypedArray()
        val checkedItemIndex = 0

        val cardMinusDialog = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
            .setTitle("카드 삭제")
            .setSingleChoiceItems(ArrayCard, checkedItemIndex) { dialog_, which ->
                Log.e("TAG", "minusDialog: ${which}", )
            }
            .setPositiveButton("삭제"){dialog, id->
                // 카드 삭제 event 넣기
                Log.e("TAG", "getSpinner: 카드 삭제 성공", )
//                activityViewModel.deleteCardData(minusCard)
                newCard = 1
                dialog.dismiss()   // 예비
            }
            .setNegativeButton("취소"){dialog, id->
                Log.e("TAG", "getSpinner: 카드 삭제 취소", )
                dialog.dismiss()
            }
            .show()

        cardMinusDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(Color.RED)
        cardMinusDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.BLACK)
    }


    private fun getSpinner() {
        activityViewModel.receiveServerCardData()
        Log.e("TAG", "getSpinner: getSpinner",)
        cardArray?.let { viewModel.takeCardData(it) }
        var adapter = SpinnerCustomAdapter(requireContext(), arrayListOf<String>())
        /*
        var adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayListOf<String>()
        )
        */
        binding.spinner.adapter = adapter
        Log.e("TAG", "getSpinner: 현재 들어가있는값 : ${arrayCardList}")
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("TAG", "onItemSelected: ${myArray[position]}", )
                Log.e("TAG", "onItemSelected: ${position}", )
                val spiltCard = myArray[position].split(" : ")
                checked = spiltCard[0]
                Log.e("TAG", "onItemSelected: ${checked}", )
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

    override fun initUI() {
        TODO("Not yet implemented")
    }

    override fun initListener() {
        TODO("Not yet implemented")
    }

    override fun initObserver() {
        TODO("Not yet implemented")
    }
}
