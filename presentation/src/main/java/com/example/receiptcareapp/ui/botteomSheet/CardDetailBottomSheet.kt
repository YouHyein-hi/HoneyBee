package com.example.receiptcareapp.ui.botteomSheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.model.remote.receive.card.CardData
import com.example.domain.model.remote.receive.card.CardDetailData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseBottomSheet
import com.example.receiptcareapp.databinding.BottomsheetCardDetailBinding
import com.example.receiptcareapp.ui.dialog.CardAddDialog
import com.example.receiptcareapp.ui.adapter.CardListAdapter
import com.example.receiptcareapp.ui.dialog.CardChangeDialog
import com.example.receiptcareapp.ui.dialog.CardDeleteDialog
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardDetailBottomSheet(
    private val cardData:CardData
    ): BaseBottomSheet<BottomsheetCardDetailBinding>(
    BottomsheetCardDetailBinding::inflate,
    "BottomsheetCardBinding"
) {
    private val viewModel: CardViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {
        viewModel.getServerCardDetilaData(cardData.uid.toString())
    }

    override fun initUI() {
    }

    override fun initListener() {
        binding.cardDetailChangeBtn.setOnClickListener {
            Log.e("TAG", "initListener idid: ${cardData.uid}", )
            viewModel.putId(cardData.uid)
            cardChangeDialog()
        }
        binding.cardDetailDeleteBtn.setOnClickListener {
            Log.e("TAG", "initListener idid: ${cardData.uid}", )
            viewModel.putId(cardData.uid)
            cardDeleteDialog()
        }


    }

    override fun initObserver() {

        viewModel.cardDetailList.observe(viewLifecycleOwner){
            val cardDetail = it?.body
            binding.cardDate = cardDetail?.let { it1 ->
                CardDetailData(it1.billCardId, it1.cardName, it1.cardAmount, it1.cardDesignId, it1.cardExpireDate,
                        it1.writerName, StringUtil.datetimeToDate(it1.writeDateTime)
                    , it1.modifierName, StringUtil.datetimeToDate(it1.modifyDateTime))
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.response.observe(viewLifecycleOwner){
            Log.e("TAG", "initObserver: 카드 리턴값 $it", )
            when(it?.status) {
                "200" -> {
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

    private fun cardChangeDialog(){
        val cardChangeDialog = CardChangeDialog(viewModel, cardData)
        cardChangeDialog.show(parentFragmentManager, "cardChangeDialog")
    }

    private fun cardDeleteDialog(){
        val cardDeleteDialog = CardDeleteDialog(viewModel, this)
        cardDeleteDialog.show(parentFragmentManager, "cardDeleteDialog")
    }

}