package com.example.receiptcareapp.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnetedState
import com.example.receiptcareapp.State.ServerState
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.measureTime

class ShowPictureFragment :
    BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
    private val viewModel: FragmentViewModel by viewModels({ requireActivity() })
    private val activityViewModel: MainViewModel by activityViewModels()
    private var checked = ""
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0

    private var cardArray : MutableMap<String, Int>? = mutableMapOf("카드1" to 1000, "카드2" to 2000)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("TAG", "viewModel.image.value : ${viewModel.image.value}")
        binding.pictureView.setImageURI(viewModel.image.value)

        // 서버와 연결 상태 초기화.
        activityViewModel.changeConnectedState(ConnetedState.DISCONNECTED)
        activityViewModel.changeServerState(ServerState.NONE)

        val dataNow = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        binding.btnDate.text = "${dataNow.format(formatterDate)}"

        //날짜 관리
        binding.btnDate.setOnClickListener {
            val cal = Calendar.getInstance()
            binding.btnDate.text = "${myYear}/${myMonth}/${myDay}"
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myYear = year
                myMonth = month + 1
                myDay = day
                binding.btnDate.text = "${myYear}/${myMonth}/${myDay}"
            }
            val dataDialog = DatePickerDialog(
                requireContext(),
                data,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dataDialog.show()
            dataDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
            dataDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLACK)

        }

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner) {
            if (it == ConnetedState.CONNECTING) {
                binding.waitingView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.waitingView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }


        //서버 연결 상태에 따른 처리 후 화면 전환.
        activityViewModel.serverState.observe(viewLifecycleOwner){
            if (it == ServerState.SUCCESS) {
                Toast.makeText(requireContext(), "전송 완료!", Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_showFragment_to_homeFragment)
            } else if (it == ServerState.FALSE) {
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            }
        }

        /** Spinner 호출 **/
        getSpinner()

        /** 카드 추가 관련 코드 **/
        val dialogView = layoutInflater.inflate(R.layout.dialog_card, null)
        val editText_cardName  = dialogView.findViewById<EditText>(R.id.dialog_cardname)
        val editText_cardPrice = dialogView.findViewById<EditText>(R.id.dialog_cardprice)
        //val dialogParentView: ViewGroup? = dialogView.parent as ViewGroup?

        binding.cardaddBtn.setOnClickListener{
            val cardAddDialog = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
                .setTitle("카드 추가")
                .setMessage("추가할 카드 이름과 초기 금액을 입력해주세요.")
                .setView(dialogView)
                .setPositiveButton("확인") { dialog, id ->
                    if(editText_cardName.text.toString() == ""){
                        //Toast.makeText(requireContext(), "카드 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "onViewCreated: 카드 이름을 입력해주세요", )
                        dialog.dismiss()
                    }
                    else if(editText_cardPrice.text.toString() == ""){
                        //Toast.makeText(requireContext(), "초기 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "onViewCreated: 초기을 입력해주세요", )
                        dialog.dismiss()
                    }
                    else{
                        cardArray?.put(editText_cardName.text.toString(), editText_cardPrice.text.toString().toInt())
                        cardArray?.let { it -> viewModel.takeCardData(it) }
                        val cardMap = mapOf<String, Int>(editText_cardName.text.toString() to editText_cardPrice.text.toString().toInt())
                        Log.e("TAG", "onViewCreated: ${cardMap}", )
                        activityViewModel.changeConnectedState(ConnetedState.CONNECTING)
                        activityViewModel.sendCard(cardMap)
                        //dialogParentView?.removeView(dialogView)
                        getSpinner()
                    }
                }
                .setCancelable(false)
                .show()

            cardAddDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
            cardAddDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLACK)
        }

        /** 카드 삭제 관련 코드 **/
        binding.cardminusBtn.setOnClickListener(){
            minusDialog()
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
                binding.btnPrice.setText(gap.format(binding.btnPrice.text.toString().toInt()))
            }
            handled
        }


        binding.sendBtn.setOnClickListener {
            Log.e("TAG", "onViewCreated: iinin")
            if (checked == "") {
                Toast.makeText(requireContext(), "카드를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnStore.text!!.isEmpty()) {
                Toast.makeText(requireContext(), "가게 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnDate.text == "날짜") {
                Toast.makeText(requireContext(), "날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnPrice.text.isEmpty()) {
                Toast.makeText(requireContext(), "금액을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (viewModel.image.value == null) {
                Toast.makeText(requireContext(), "사진이 비었습니다.\n초기화면으로 돌아갑니다.", Toast.LENGTH_SHORT)
                    .show()
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_showFragment_to_homeFragment)

            } else {
                //연결상태로 변경
                activityViewModel.changeConnectedState(ConnetedState.CONNECTING)
                val myLocalDateTime = LocalDateTime.of(
                    myYear,
                    myMonth,
                    myDay,
                    LocalDateTime.now().hour,
                    LocalDateTime.now().minute,
                    LocalDateTime.now().second
                )
                activityViewModel.sendData(
                    date = myLocalDateTime,
                    amount = binding.btnPrice.text.toString(),
                    card = checked,
                    picture = viewModel.image.value!!,
                    pictureName = binding.btnStore.text.toString()
                )
            }
        }

        //취소버튼
        binding.cancleBtn.setOnClickListener {
            findNavController().navigate(R.id.action_showFragment_to_homeFragment)

        }

    }

    fun minusDialog(){
        val array : Map<String, Int>? = viewModel.card.value
        Log.e("TAG", "minusDialog: ${array}", )
        val ArrayCardList : MutableList<String> = mutableListOf()
        var ArrayCard : Array<String>

        if (array != null) {
            for(i in array){
                ArrayCardList?.add("${i.key} : ${i.value}")
                Log.e("TAG", "minusDialog: ${ArrayCardList}", )
            }
        }
        ArrayCard = ArrayCardList.toTypedArray()
        val checkedItemIndex = 0

        val cardMinusDialog = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
            .setTitle("카드 삭제")
            .setSingleChoiceItems(ArrayCard, checkedItemIndex) { dialog_, which ->
                Log.e("TAG", "minusDialog: ${which}", )
            }
            .setPositiveButton("삭제한다"){dialog, id->
                // 카드 삭제 event 넣기
                Log.e("TAG", "getSpinner: 카드 삭제 성공", )
                dialog.dismiss()   // 예비
            }
            .setNegativeButton("삭제하지 않는다"){dialog, id->
                Log.e("TAG", "getSpinner: 카드 삭제 취소", )
                dialog.dismiss()
            }
            .show()

        cardMinusDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(Color.RED)
        cardMinusDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.BLACK)
    }


    fun getSpinner(){
        cardArray?.let { viewModel.takeCardData(it) }
        val array : Map<String, Int>? = viewModel.card.value
        val ArrayCard : MutableList<String> = mutableListOf()

        if (array != null) {
            for(i in array){
                ArrayCard?.add("${i.key} : ${i.value}")
            }
        }

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ArrayCard)  // ArrayAdapter에 item 값을 넣고 spinner만 보여주면 되는데 그게 안됨 ArrayAdapter에 대해 알아보기
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spiltCard = ArrayCard[position].split(" : ")
                checked = spiltCard[0]
                Log.e("TAG", "onItemSelected: ${checked}", )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


    }
}
