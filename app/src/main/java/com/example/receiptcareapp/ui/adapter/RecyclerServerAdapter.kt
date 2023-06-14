package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.receiptcareapp.databinding.ServerItemBinding

/**
 * 2023-02-06
 * pureum
 */
class RecyclerServerAdapter(

) :RecyclerView.Adapter<RecyclerServerAdapter.MyAdapter>(){

    lateinit var onServerSaveClick : (DomainReceiveAllData)->Unit
    private lateinit var serverBinding:ServerItemBinding
    var dataList = mutableListOf<DomainReceiveAllData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyAdapter(private val binding: ServerItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: DomainReceiveAllData){
            binding.storeName.text = "${list.storeName}"
            binding.cardName.text = "${list.cardName}"
            binding.amount.text = " ${list.amount}"
            binding.date.text = "${list.date}"
            binding.listLayout.setOnClickListener{ onServerSaveClick(list) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        serverBinding = ServerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(serverBinding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size
}