package com.example.receiptcareapp.fragment.recyclerFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentRecyclerShowBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel

class RecyclerShowFragment : BaseFragment<FragmentRecyclerShowBinding>(FragmentRecyclerShowBinding::inflate) {

    private val fragmentViewModel : FragmentViewModel by viewModels()
    private val activityViewModel : MainViewModel by activityViewModels()
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewModel.showData.observe(viewLifecycleOwner){
            //binding.imageView.setImageURI("it.picture")
            binding.cardAmount.text = "${it.card} : ${it.amount}"
        }

        binding.removeBtn.setOnClickListener{
            AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
                .setTitle("")
                .setMessage("정말 삭제하실 건가요?\n삭제한 데이터는 복구시킬 수 없어요.")
                .setPositiveButton("닫기"){dialog, id->

                }
                .setNegativeButton("삭제"){dialog, id->
                    activityViewModel.deleteData(binding.date.text.toString())
                    findNavController().popBackStack()
                }.show()
        }
    }

}