package com.example.receiptcareapp.fragment.recyclerFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.DomainRoomData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.time.LocalDateTime
import kotlin.math.log


class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    private val activityViewModel: MainViewModel by activityViewModels()
    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val adapter:Adapter = Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        activityViewModel.getAllData()

        activityViewModel.getRoomData.observe(viewLifecycleOwner){
            adapter.dataList = it
            binding.noneData.isVisible = adapter.dataList.isEmpty()
        }

//        adapter.getPicture = {
//            getPicture(it)
//        }

        adapter.onSaveClic = {
            fragmentViewModel.myShowData(it)
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }



        binding.imageBack.setOnClickListener{
            findNavController().navigate(R.id.action_recyclerFragment_to_homeFragment)
        }
    }



    fun initRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter
    }
}