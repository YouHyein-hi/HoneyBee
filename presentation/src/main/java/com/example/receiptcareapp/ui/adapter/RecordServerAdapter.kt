package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.ui.recycler.ServerRecyclerData
import com.example.receiptcareapp.databinding.ItemBillListServerBinding

/**
 * 2023-02-06
 * pureum
 */
class RecordServerAdapter:RecyclerView.Adapter<RecordServerAdapter.MyAdapter>(){

    lateinit var onServerSaveClick : (ServerRecyclerData)->Unit
    private lateinit var serverBinding:ItemBillListServerBinding
    var dataList = mutableListOf<ServerRecyclerData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyAdapter(private val binding: ItemBillListServerBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: ServerRecyclerData){
            with(binding){
                serverRecyclerData = list
                listLayout.setOnClickListener{ onServerSaveClick(list) }
                if(list.billCheck) { billCheckLayout.bringToFront() }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        serverBinding = ItemBillListServerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(serverBinding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size
}