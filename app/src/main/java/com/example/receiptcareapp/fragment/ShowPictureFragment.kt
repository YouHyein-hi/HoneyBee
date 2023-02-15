package com.example.receiptcareapp.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentShowPictureBinding
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import java.util.*

class ShowPictureFragment : Fragment() {
    private val binding : FragmentShowPictureBinding by lazy { FragmentShowPictureBinding.inflate(layoutInflater) }
    private val viewModel : FragmentViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        binding.date.setOnClickListener{
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day -> binding.date.text = "${year}/${month}/${day}" }
            DatePickerDialog(requireContext(),data,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.button.setOnClickListener{ NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment) }
        binding.button2.setOnClickListener{ NavHostFragment.findNavController(this).navigate(R.id.action_showFragment_to_homeFragment) }
        return binding.root
    }
}