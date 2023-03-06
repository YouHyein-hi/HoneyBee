package com.example.receiptcareapp.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.util.*

class ShowPictureFragment : BaseFragment<FragmentShowPictureBinding>(FragmentShowPictureBinding::inflate) {
    private val viewModel : FragmentViewModel by viewModels({ requireActivity() })
    private val activityViewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pageNum = viewModel.pageNum.value
        when(pageNum){
            1 -> {
                Log.e("TAG", "onCreateView: pageNum(1 : CameraFragment) 넘어옴", )
                binding.pictureView.setImageBitmap(viewModel.picture.value)
            }
            2 -> {
                Log.e("TAG", "onCreateView: pageNum(2: GalleryFragment) 넘어옴", )
                binding.pictureView.setImageURI(viewModel.image.value)
            }
        }

        binding.btnDate.setOnClickListener{
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day -> binding.btnDate.text = "${year}/${month+1}/${day}" }
            DatePickerDialog(requireContext(),data,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnCancel.setOnClickListener{

            println(binding.radioGroup.clearCheck())
//            if(binding.radioGroup.is) {
//                println()
//                //라디오버튼 입력 권유 메시지
//            }else if(binding.date.text == "날짜") {
//                //날짜 입력 권유 메시지
//            }

//            binding.radioGroup.setOnCheckedChangeListener{ _, checkedId ->
//                when(checkedId){
//                    R.id.radioButton_card1 -> {}
//                    R.id.radioButton_card2 -> {}
//                    R.id.radioButton_card3 -> {}
//                    else -> {}
//                }
//            }
//            activityViewModel.insertData()
            NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment)
        }
        binding.btnSend.setOnClickListener{ NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment) }
    }



}