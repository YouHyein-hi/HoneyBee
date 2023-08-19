package com.example.receiptcareapp.ui.botteomSheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseBottomSheet
import com.example.receiptcareapp.databinding.BottomsheetCardBinding
import com.example.receiptcareapp.ui.dialog.CardAddDialog
import com.example.receiptcareapp.ui.adapter.CardAdapter
import com.example.receiptcareapp.viewModel.dialogViewModel.HomeCardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 2023-03-22
 * pureum
 */
@AndroidEntryPoint
class CardBottomSheet(
    private val viewModel: HomeCardViewModel
): BaseBottomSheet<BottomsheetCardBinding>(
    BottomsheetCardBinding::inflate,
    "BottomsheetCardBinding"
) {
    private val adapter: CardAdapter = CardAdapter()
//    private val viewModel : HomeCardViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {
        //서버 데이터 불러오기
        viewModel.getServerCardData()
    }

    override fun initUI() {
        binding.cardCount = "0"
        Log.e("TAG", "initUI homecardBottomSheet: ${binding.cardCount}")
        //initUI 부분
        binding.cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerview.adapter = adapter
    }

    override fun initListener() {
        //서버 카드 추가 다이얼로그
        binding.cardCount = "0"
        binding.addBtn.setOnClickListener{
            val cardAddDialog = CardAddDialog(viewModel)
            cardAddDialog.show(parentFragmentManager, "CardAddDialog")
        }
    }

    override fun initObserver() {
        binding.cardCount = "0"

        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.cardList.observe(viewLifecycleOwner){
            adapter.dataList = it?.body!!.toMutableList()
//            when(it){
//                ResponseState.SUCCESS -> {
//                    setCenterText("추가 성공!", true)
//                }
//                else -> {
//                    setCenterText("전송 실패!", true)
//                }
//            }
        }
    }

    fun setCenterText(text:String, state:Boolean){
        binding.centerStateTxt.text = text
        binding.centerStateTxt.isVisible = state
    }


}