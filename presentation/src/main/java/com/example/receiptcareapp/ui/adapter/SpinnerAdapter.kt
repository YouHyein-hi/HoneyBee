package com.example.receiptcareapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.*
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import com.example.domain.model.remote.receive.card.CardSpinnerData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.SpinnerCustomItemLayoutBinding
import com.example.receiptcareapp.databinding.SpinnerCustomLayoutBinding

class SpinnerAdapter(context: Context, items: ArrayList<CardSpinnerData>) : ArrayAdapter<CardSpinnerData>(context, R.layout.spinner_custom_item_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCustomLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.spinnerItem.height = 35 // 높이를 35dp로 설정합니다.

        val item = getItem(position)
        binding.data = item
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCustomItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.spinnerItem.height = 35 // 높이를 35dp로 설정합니다.

        val item = getItem(position)
        binding.data = item
        return binding.root
    }
}