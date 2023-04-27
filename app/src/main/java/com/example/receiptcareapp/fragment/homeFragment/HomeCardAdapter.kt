package com.example.receiptcareapp.fragment.homeFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.receiptcareapp.databinding.CardItemBinding

/**
 * 2023-03-22
 * pureum
 */
class HomeCardAdapter : RecyclerView.Adapter<HomeCardAdapter.MyHolder>(){

    lateinit var onLocalSaveClic : (DomainReceiveCardData)->Unit
    lateinit var longClick : (DomainReceiveCardData)->Unit
    private lateinit var cardBinding: CardItemBinding
    var dataList = mutableListOf<DomainReceiveCardData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }
    inner class MyHolder(private val binding : CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainReceiveCardData) {
            Log.e("TAG", "bind: inin", )
            binding.cardName.text = item.cardName
            binding.amount.text = "${item.cardAmount} 원"
            binding.body.setOnClickListener{ onLocalSaveClic(item) }
            binding.body.setOnLongClickListener(View.OnLongClickListener {
                longClick(item)
                return@OnLongClickListener true
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        cardBinding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(cardBinding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
     holder.bind(dataList[position])
    }
    override fun getItemCount(): Int = dataList.size
}