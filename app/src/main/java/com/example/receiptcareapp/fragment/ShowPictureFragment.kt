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
import android.widget.ImageView
import android.widget.Toast
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
                myMonth = month
                myDay = day
                binding.btnDate.text = "${myYear}/${myMonth}/${myDay}"
            }
            DatePickerDialog(requireContext(),data,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }




        binding.radioGroup.setOnCheckedChangeListener{ _, checkedId ->
            when(checkedId){
                R.id.radioButton_card1 -> {
                    Log.e("TAG", "onViewCreated: 1", )
                    checked = binding.radioButtonCard1.text.toString()}
                R.id.radioButton_card2 -> {
                    Log.e("TAG", "onViewCreated: 2", )
                    checked = binding.radioButtonCard2.text.toString()}
                R.id.radioButton_card3 -> {
                    Log.e("TAG", "onViewCreated: 3", )
                    checked = binding.radioButtonCard3.text.toString()}
                else -> {}
            }
        }

        binding.price.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var myS = s
                if (myS != null) {
                    if(myS.contains(",")) myS.toString().replace(",","")
                    if(myS.length % 3 == 0){
                        val gap = DecimalFormat("#,###")
                        //Log.e("TAG", "onTextChanged:$myS")
                        Log.e("TAG", "onTextChanged: ${gap.format(myS.toString().toInt())}", )
                        //binding.price.text = gap.format(myS.toString().toInt())
                    }
                }
            }

        })


        binding.sendBtn.setOnClickListener{
            if(checked=="") {
                Toast.makeText(requireContext(), "카드를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(binding.btnDate.text == "날짜"){
                Toast.makeText(requireContext(), "날짜를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(binding.price.text.toString() == "금액"){
                Toast.makeText(requireContext(), "금액을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(viewModel.picture.value==null){
                Toast.makeText(requireContext(), "사진이 비었습니다.\n초기화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            } else{
                val myLocalDateTime = LocalDateTime.of(myYear, myMonth, myDay, LocalDateTime.now().hour, LocalDateTime.now().minute)
                activityViewModel.sendData(
                    date = myLocalDateTime,
                    amount = binding.price.text.toString(),
                    card = checked,
                    picture = viewModel.bytePicture.value!!
                )
//                activityViewModel.insertData()
                NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
            }
        }
        binding.cancleBtn.setOnClickListener{ NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment) }
    }
}