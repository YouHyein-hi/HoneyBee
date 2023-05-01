package com.example.receiptcareapp.fragment.recyclerFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.local.DomainRoomData
import com.example.receiptcareapp.databinding.LocalItemBinding

/**
 * 2023-03-16
 * pureum
 */
class LocalAdapter: RecyclerView.Adapter<LocalAdapter.MyAdapter>(){

    lateinit var onLocalSaveClic : (DomainRoomData)->Unit
    private lateinit var localBinding: LocalItemBinding
    var dataList = mutableListOf<DomainRoomData>()
        set(value){
            field = value.reversed().toMutableList()
            notifyDataSetChanged()
        }

    inner class MyAdapter(private val binding: LocalItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list:DomainRoomData){
//            var myDate = list.date.split("-","T")
            binding.cardName.text = "${list.cardName} :"
            binding.amount.text = " ${list.amount}"
            binding.date.text = list.date
//            binding.date.text = "${myDate[0]}.${myDate[1]}.${myDate[2]} / ${myDate[3]}"
//            binding.date.text = "${list.date}"
//            binding.picture.setImageURI(list.file)
            binding.listLayout.setOnClickListener{ onLocalSaveClic(list) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        localBinding = LocalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(localBinding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size

}