package com.example.receiptcareapp.fragment.recyclerFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.DomainReceiveAllData
import com.example.receiptcareapp.databinding.ServerItemBinding
import com.example.receiptcareapp.dto.ShowData

/**
 * 2023-02-06
 * pureum
 */
class ServerAdapter(

) :RecyclerView.Adapter<ServerAdapter.MyAdapter>(){

    lateinit var onServerSaveClick : (DomainReceiveAllData)->Unit
    private lateinit var serverBinding:ServerItemBinding
    var dataList = listOf<DomainReceiveAllData>()
    set(value){
        field = value.reversed()
        notifyDataSetChanged()
    }

    inner class MyAdapter(private val binding: ServerItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list:DomainReceiveAllData){
            val myDate = list.date.split("-","T")
            binding.cardName.text = "${list.cardName} :"
            binding.amount.text = " ${list.amount}"
            binding.date.text = "${myDate[0]}.${myDate[1]}.${myDate[2]} / ${myDate[3]}"
            //binding.picture.setImageURI(list.picture.toUri())
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