package com.example.receiptcareapp.fragment.recyclerFragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.fragment.app.activityViewModels
import com.example.domain.model.DomainRoomData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.viewModel.MainViewModel

class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    private val activityViewModel:MainViewModel by activityViewModels()
    private val adapter:Adapter = Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.getRoomData.observe(viewLifecycleOwner){
            adapter.dataList = it
        }

        binding.button3.setOnClickListener{
            activityViewModel.insertData(DomainRoomData("pureum","pureum", "pureum"))
            activityViewModel.getAllData()
        }
        activityViewModel.getRoomData.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
        }
    }

}