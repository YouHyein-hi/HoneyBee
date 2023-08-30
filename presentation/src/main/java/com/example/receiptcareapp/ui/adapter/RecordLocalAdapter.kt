package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.ui.recycler.LocalRecyclerData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.databinding.ItemBillListLocalBinding

/**
 * 2023-03-16
 * pureum
 */
class RecordLocalAdapter: RecyclerView.Adapter<RecordLocalAdapter.MyAdapter>(){

    lateinit var onLocalSaveClick : (LocalRecyclerData)->Unit
    private lateinit var localBinding: ItemBillListLocalBinding
    var dataList = mutableListOf<LocalRecyclerData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyAdapter(private val binding: ItemBillListLocalBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: LocalRecyclerData){
            //TODO 데이터 클래스로 묶어서 XML로 데이터 바인딩 연결
            binding.recordItemStoreTxt.text = list.storeName
            binding.recordItemCardTxt.text = "${list.cardName}카드"
            binding.recordItemAmountTxt.text = list.amount
            binding.recordItemDateTxt.text = StringUtil.changeDate(list.date)
            binding.listLayout.setOnClickListener{ onLocalSaveClick(list) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        localBinding = ItemBillListLocalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(localBinding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size

}