package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RecyclerShowData
import com.example.receiptcareapp.databinding.ItemLocalBillListBinding

/**
 * 2023-03-16
 * pureum
 */
class RecordLocalAdapter: RecyclerView.Adapter<RecordLocalAdapter.MyAdapter>(){

    lateinit var onLocalSaveClic : (RecyclerShowData)->Unit
    private lateinit var localBinding: ItemLocalBillListBinding
    var dataList = mutableListOf<RecyclerShowData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyAdapter(private val binding: ItemLocalBillListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list:RecyclerShowData){
            binding.storeName.text = list.storeName
            binding.cardName.text = list.cardName
            binding.amount.text = list.amount
            binding.dateTxt.text = list.date
            binding.listLayout.setOnClickListener{ onLocalSaveClic(list) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        localBinding = ItemLocalBillListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(localBinding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size

}