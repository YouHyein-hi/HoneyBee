package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.receiptcareapp.databinding.ServerItemBinding

/**
 * 2023-02-06
 * pureum
 */
class RecordServerAdapter(

) :RecyclerView.Adapter<RecordServerAdapter.MyAdapter>(){

    lateinit var onServerSaveClick : (DomainReceiveAllData)->Unit
    private lateinit var serverBinding:ServerItemBinding
    var dataList = mutableListOf<DomainReceiveAllData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyAdapter(private val binding: ServerItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: DomainReceiveAllData){
            with(binding){
                storeName.text = list.storeName
                cardName.text = list.cardName
                amount.text = list.amount
                date.text = list.date
                listLayout.setOnClickListener{ onServerSaveClick(list) }
                if(list.billCheck) {
                    billCheckLayout.visibility = View.VISIBLE
                    billCheckLayout.bringToFront()
                }
                else billCheckLayout.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        serverBinding = ServerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(serverBinding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size
}