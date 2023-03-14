package com.example.receiptcareapp.fragment.recyclerFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.DomainRoomData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.time.LocalDateTime

class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    private val activityViewModel:MainViewModel by activityViewModels()
    private val fragmentViewModel:FragmentViewModel by viewModels()
    private val adapter:Adapter = Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        activityViewModel.getAllData()

        activityViewModel.getRoomData.observe(viewLifecycleOwner){
            adapter.dataList = it
            binding.noneData.isVisible = adapter.dataList.isEmpty()
        }

        adapter.onSaveClic = {
            fragmentViewModel.myShowData(it)
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }
    }



    fun initRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter
    }
}