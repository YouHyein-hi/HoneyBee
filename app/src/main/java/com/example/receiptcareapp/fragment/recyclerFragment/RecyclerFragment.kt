package com.example.receiptcareapp.fragment.recyclerFragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.createBitmap
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

class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    private val activityViewModel:MainViewModel by activityViewModels()
    private val fragmentViewModel:FragmentViewModel by viewModels()
    private val adapter:Adapter = Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initRecyclerView()

        activityViewModel.getRoomData.observe(viewLifecycleOwner){
            adapter.dataList = it
        }

        activityViewModel.getRoomData.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
        }

        adapter.onSaveClic = {
            fragmentViewModel.myShowData(it)
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }
    }



    fun initRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter
        adapter.dataList = listOf(
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
            DomainRoomData("pureum", "hello", "hello"),
        )
    }
}