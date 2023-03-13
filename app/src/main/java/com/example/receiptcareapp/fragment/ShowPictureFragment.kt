package com.example.receiptcareapp.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.util.*

class ShowPictureFragment : BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
    private val viewModel : FragmentViewModel by viewModels({ requireActivity() })
    private val activityViewModel : MainViewModel by activityViewModels()
    private var checked = ""
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pictureView.setImageURI(viewModel.image.value)

        binding.btnDate.setOnClickListener{
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myYear = year
                myMonth = month+1
                myDay = day
                binding.btnDate.text = "${myYear}/${myMonth}/${myDay}"
            }
            DatePickerDialog(requireContext(),data,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        /*** Spinner 관련 코드 ***/
        ArrayAdapter.createFromResource(
            requireContext(), R.array.card_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0 ->{
                        Log.e("TAG", "onItemSelected: 카드1 선택", )
                        checked = "card1"
                    }
                    1 ->{
                        Log.e("TAG", "onItemSelected: 카드2 선택", )
                        checked = "card2"
                    }
                    2 ->{
                        Log.e("TAG", "onItemSelected: 카드3 선택", )
                        checked = "card3"
                    }
                    3 ->{
                        Log.e("TAG", "onItemSelected: 카드4 선택", )
                        checked = "card4"
                    }
                }
            }
        }

        binding.btnPrice.setOnClickListener{
            if(binding.btnPrice.text.contains(",")){
                binding.btnPrice.setText(binding.btnPrice.text.toString().replace(",",""))
            }
        }

        binding.btnPrice.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if(actionId==EditorInfo.IME_ACTION_DONE){
                val gap = DecimalFormat("#,###")
                binding.btnPrice.setText(gap.format(binding.btnPrice.text.toString().toInt()))
            }
            handled
        }

        binding.sendBtn.setOnClickListener{
            if(checked=="") {
                Toast.makeText(requireContext(), "카드를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(binding.btnDate.text == "날짜"){
                Toast.makeText(requireContext(), "날짜를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(binding.btnPrice.text.toString() == "금액" || binding.btnPrice.text.toString() == ""){
                Toast.makeText(requireContext(), "금액을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(viewModel.image.value==null){
                Toast.makeText(requireContext(), "사진이 비었습니다.\n초기화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            } else{
                val myLocalDateTime = LocalDateTime.of(myYear, myMonth, myDay, LocalDateTime.now().hour, LocalDateTime.now().minute)
                activityViewModel.sendData(
                    date = myLocalDateTime,
                    amount = binding.btnPrice.text.toString(),
                    card = checked,
                    picture = viewModel.image .value!!
                )
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            }
        }
        binding.cancleBtn.setOnClickListener{ NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment) }
    }
}