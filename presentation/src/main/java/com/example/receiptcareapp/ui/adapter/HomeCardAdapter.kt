package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.ItemHomeCardListBinding

class HomeCardAdapter : RecyclerView.Adapter<HomeCardAdapter.MyHolder>(){

    private lateinit var binding: ItemHomeCardListBinding

    var dataList = mutableListOf<DomainReceiveCardData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyHolder(private val binding : ItemHomeCardListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainReceiveCardData, position:Int) {
            binding.homeCardname.text = item.cardName
            binding.amount = item.cardAmount
            when(position) {
                1 -> binding.cardIcon.setImageResource(R.drawable.icon_card_1)
                2 -> binding.cardIcon.setImageResource(R.drawable.icon_card_2)
                3 -> binding.cardIcon.setImageResource(R.drawable.icon_card_3)
                else -> binding.cardIcon.setImageResource(R.drawable.icon_card_4)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        binding = ItemHomeCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(dataList[position], position%4 )
    }

    override fun getItemCount(): Int = dataList.size

}