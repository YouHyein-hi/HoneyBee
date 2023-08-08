package com.example.receiptcareapp.ui.adapter

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
class CardAdapter : RecyclerView.Adapter<CardAdapter.MyHolder>(){

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
            binding.cardName = item.cardName
            binding.amount = item.cardAmount
            binding.billCheckDate = "10" + "일"
            // TODO DomainReceiveCardData에 billCheckDate를 추가해서 item.billCheckDate 해서 가져오면 될듯~!
            // TODO DomainReceiveCardData에 있는 값 가져올 거면 그냥 서버에 파라미터 추가되는 거 쓰면 되는 거 아닌가?
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