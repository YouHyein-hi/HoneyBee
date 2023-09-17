package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.remote.receive.card.CardData
import com.example.receiptcareapp.databinding.ItemCardBinding

/**
 * 2023-03-22
 * pureum
 */

class CardListAdapter : RecyclerView.Adapter<CardListAdapter.MyHolder>(){

    lateinit var onCardClick : (CardData)->Unit
    private lateinit var cardBinding: ItemCardBinding
    var dataList = mutableListOf<CardData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }
    inner class MyHolder(private val binding : ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardData) {
            binding.cardData = item
            binding.cardLayout.setOnClickListener{ onCardClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        cardBinding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(cardBinding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
     holder.bind(dataList[position])
    }
    override fun getItemCount(): Int = dataList.size
}