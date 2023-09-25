package com.example.receiptcareapp.ui.dialog

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.remote.send.card.SendCardData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogCardAddBinding
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.CardViewModel
import com.example.domain.model.ui.dateTime.DateData
import com.example.receiptcareapp.ui.adapter.CardImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardAddDialog(
    private val cardViewModel: CardViewModel
) : BaseDialog<DialogCardAddBinding>(DialogCardAddBinding::inflate) {

    private val viewModel : CardAddViewModel by viewModels()
    private lateinit var dateData : DateData
    private var cardImage = 1
    private val imageResIds = listOf( R.drawable.card_image_1, R.drawable.card_image_2, R.drawable.card_image_3)
    private val adapter by lazy { CardImageAdapter(imageResIds)}

    override fun initData() {}

    override fun initUI() {
        val date = StringUtil.dateNow()
        binding.cardAddDateDatePicker.init(date.year, date.monthValue-1, date.dayOfMonth, null)
        binding.image = cardImage
        binding.cardAddImageRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.cardAddImageRecyclerView.adapter = adapter
    }

    override fun initListener() {
        with(binding){
            cardAddOkBtn.setOnClickListener{
                var price = cardAddPriceEdit.text.toString()
                val priceZero = price.count { it == '0' }
                dateData = DateData(
                    binding.cardAddDateDatePicker.year,
                    binding.cardAddDateDatePicker.month+1,
                    binding.cardAddDateDatePicker.dayOfMonth
                )
                val myDateTime = StringUtil.myLocalDateFunction(dateData.year, dateData.month, dateData.day)

                if(cardAddNameEdit.text.toString() == ""){
                    showShortToast(getString(R.string.dialog_cardAdd_name))
                }
                else if(cardAddPriceEdit.text.toString() == ""){
                    showShortToast(getString(R.string.dialog_cardAdd_price))
                }
                else if(priceZero == price.length){
                    showShortToast(getString(R.string.dialog_cardAdd_price_zero))
                }
                else{
                    if (price.contains(","))
                        price = price.replace(",", "")
                    cardViewModel.insertServerCardData(SendCardData(cardAddNameEdit.text.toString(), price.toInt(), myDateTime!!, cardImage))
                    dismiss()
                }
            }

            cardAddCancelBtn.setOnClickListener{
                dismiss()
            }

            adapter.onCardImageClick = {
                cardImage = it
                image = it
            }

        }
    }

    override fun initObserver() {
        viewModel.response.observe(viewLifecycleOwner){
            when(it){
                ResponseState.UPDATE_SUCCESS -> {
                    cardViewModel.getServerCardData()
                    dismiss()
                }
                else->{}
            }
        }

        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }


}