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
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 2023-03-22
 * pureum
 */
@AndroidEntryPoint
class CardBottomSheet(
    private val homeViewModel: HomeViewModel
): BaseBottomSheet<BottomsheetCardBinding>(
    BottomsheetCardBinding::inflate,
    "BottomsheetCardBinding"
) {
    private val adapter: CardAdapter = CardAdapter()
    private val viewModel: CardViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {
        //서버 데이터 불러오기
        adapter.dataList.clear()
        viewModel.getServerCardData()
    }

    override fun initUI() {
        binding.cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerview.adapter = adapter
    }

    override fun initListener() {
        binding.cardAddBtn.setOnClickListener{
            cardAddDialog()
        }
    }

    override fun initObserver() {
        //TODO databinding으로 옵져버하게 ,, 어떻게 뺄지 고민
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.cardList.observe(viewLifecycleOwner){
            adapter.dataList = it?.body!!.toMutableList()
            changeEmptyTxt(it.body!!.isEmpty())
        }

        viewModel.response.observe(viewLifecycleOwner){
            Log.e("TAG", "initObserver: 카드 리턴값 $it", )
            when(it?.status) {
                "200" -> {
                    showLongToast("카드 추가 완료!")
                    viewModel.getServerCardData()
                }
                else -> {showLongToast("카드 추가 실패..")}
            }
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun changeEmptyTxt(state:Boolean){
        binding.cardEmptyTxt.isVisible = state
    }

    private fun cardAddDialog(){
        val cardAddDialog = CardAddDialog(viewModel)
        cardAddDialog.show(parentFragmentManager, "CardAddDialog")
    }
}