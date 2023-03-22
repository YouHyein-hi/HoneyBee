package com.example.receiptcareapp.fragment.homeFragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.SettingBottomSheetBinding
import com.example.receiptcareapp.dto.ServerCardData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * 2023-03-22
 * pureum
 */
class HomeCardBottomSheet : BottomSheetDialogFragment() {
    private val binding : SettingBottomSheetBinding by lazy {
        SettingBottomSheetBinding.inflate(layoutInflater)
    }
    private val adapter : HomeCardAdapter = HomeCardAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val list = mutableListOf(
            ServerCardData("나라사랑 카드", 10000),
            ServerCardData("선민사랑 카드", 5555)
        )

        binding.cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerview.adapter = adapter
        adapter.dataList = list
        adapter.onLocalSaveClic = {
            Log.e("TAG", "onCreateView: 아트 다이얼로그로 재전송기능 만들어야 함", )
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

    }
}