package com.example.receiptcareapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.receiptcareapp.databinding.ItemCardhomeBinding

class CardHomeAdapter : RecyclerView.Adapter<CardHomeAdapter.MyHolder>(){

    private lateinit var binding: ItemCardhomeBinding
    var dataList = mutableListOf<DomainReceiveCardData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyHolder(private val binding : ItemCardhomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainReceiveCardData) {
            Log.e("TAG", "bind: inin", )
            Log.e("TAG", "bind: ${item.cardName}, ${item.cardAmount}", )
            binding.homeCardname.text = item.cardName
            binding.homeCardprice.text = item.cardAmount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        binding = ItemCardhomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

}