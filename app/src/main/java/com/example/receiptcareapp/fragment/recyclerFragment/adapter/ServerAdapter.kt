package com.example.receiptcareapp.fragment.recyclerFragment.adapter

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.receiptcareapp.databinding.ServerItemBinding

/**
 * 2023-02-06
 * pureum
 */
class ServerAdapter(

) :RecyclerView.Adapter<ServerAdapter.MyAdapter>(){

    lateinit var onServerSaveClick : (DomainReceiveAllData)->Unit
    private lateinit var serverBinding:ServerItemBinding
    var dataList = mutableListOf<DomainReceiveAllData>()
    set(value){
        field = value.reversed().toMutableList()
        notifyDataSetChanged()
    }

    inner class MyAdapter(private val binding: ServerItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: DomainReceiveAllData){
//            Log.e("TAG", "bind: ${list.file}")
            binding.cardName.text = "${list.cardName} :"
            binding.amount.text = " ${list.amount}"
            binding.date.text = list.date
//            binding.date.text = "${list.date}"
            Log.e("TAG", "bind: ${String(list.file.toByteArray())}")
            Log.e("TAG", "bind: ${Uri.parse(String(list.file.toByteArray()))}")

            BitmapFactory.decodeByteArray(list.file.toByteArray(),0,list.file.toByteArray().size)

//            binding.picture.setImageURI(Uri.parse(String(list.file.toByteArray())))
            binding.picture.setImageBitmap(BitmapFactory.decodeByteArray(list.file.toByteArray(),0,list.file.toByteArray().size))
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