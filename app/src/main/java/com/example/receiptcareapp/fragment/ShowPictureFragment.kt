package com.example.receiptcareapp.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.view.marginStart
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import okhttp3.internal.concat
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class ShowPictureFragment : BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
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

        activityViewModel.isConnected("false")

        binding.btnDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myYear = year
                myMonth = month + 1
                myDay = day
                binding.btnDate.text = "${myYear}/${myMonth}/${myDay}"
            }
            DatePickerDialog(
                requireContext(),
                data,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //프로그래스 바 컨트롤
        activityViewModel.isConnected.observe(viewLifecycleOwner) {

            if (it == "true") {
                binding.waitingView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == "pass") {
                Toast.makeText(requireContext(), "전송 완료!", Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            }else if(it=="failed"){
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            }
            else{
                binding.waitingView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }


        /** Spinner 호출 **/
        getSpinner()

        /** 카드 추가 관련 코드 **/
        binding.cardaddBtn.setOnClickListener {
            val editText = EditText(requireContext())
            editText.gravity = Gravity.CENTER
            //editText.marginStart
            AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
                .setTitle("카드 추가")
                .setMessage("추가할 카드를 적어주세요.")
                .setView(editText)
                .setPositiveButton("확인") { dialog, id ->
                    cardArray?.put(editText.text.toString(), 3000)
                    cardArray?.let { it -> viewModel.takeCardData(it) }
                    getSpinner()
                }.show()
        }

        binding.btnPrice.setOnClickListener {
            if (binding.btnPrice.text.contains(",")) {
                binding.btnPrice.setText(binding.btnPrice.text.toString().replace(",", ""))
                binding.btnPrice.setSelection(binding.btnPrice.text.length)
            }
        }

        binding.btnPrice.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val gap = DecimalFormat("#,###")
                binding.btnPrice.setText(gap.format(binding.btnPrice.text.toString().toInt()))
            }
            handled
        }


        binding.sendBtn.setOnClickListener{
            if(checked=="") {
                Toast.makeText(requireContext(), "카드를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(binding.btnStore.text!!.isEmpty()){
                Toast.makeText(requireContext(), "가게 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(binding.btnDate.text == "날짜"){
                Toast.makeText(requireContext(), "날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(binding.btnPrice.text.isEmpty()){
                Toast.makeText(requireContext(), "금액을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(viewModel.image.value==null){
                Toast.makeText(requireContext(), "사진이 비었습니다.\n초기화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            } else{
                activityViewModel.isConnected("true")
                val myLocalDateTime = LocalDateTime.of(myYear, myMonth, myDay, LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)
                activityViewModel.sendData(
                        date = myLocalDateTime,
                        amount = binding.btnPrice.text.toString(),
                        card = checked,
                        picture = viewModel.image.value!!,
                        pictureName = binding.btnStore.text.toString()
                    )
            }
        }
        binding.cancleBtn.setOnClickListener {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_showFragment_to_homeFragment)
        }

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
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checked = "."//binding.spinner.toString()
                //Log.e("TAG", "onItemSelected: ${checked},, ",)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}