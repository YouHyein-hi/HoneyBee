package com.example.receiptcareapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.domain.model.remote.receive.card.CardSpinnerData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.SpinnerCustomItemLayoutBinding
import com.example.receiptcareapp.databinding.SpinnerCustomLayoutBinding

class SpinnerAdapter(context: Context, items: ArrayList<CardSpinnerData>) : ArrayAdapter<CardSpinnerData>(context, R.layout.spinner_custom_item_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCustomLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.spinnerItem.height = 35 // 높이를 35dp로 설정합니다.

        //TODO 데이터 클래스로 묶어서 XML로 데이터 바인딩 연결, 텍스트 뷰를 두개만들던가 하나에 넣던가
        val item = getItem(position)
        binding.data = item
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCustomItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.spinnerItem.height = 35 // 높이를 35dp로 설정합니다.

        val item = getItem(position)
        //TODO 데이터 클래스로 묶어서 XML로 데이터 바인딩 연결
        binding.data = item
        return binding.root
    }
}