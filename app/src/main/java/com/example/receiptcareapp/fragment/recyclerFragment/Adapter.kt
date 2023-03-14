package com.example.receiptcareapp.fragment.recyclerFragment

import android.content.Entity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.DomainRoomData
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import com.example.receiptcareapp.databinding.ListBinding
import kotlinx.coroutines.NonDisposableHandle.parent

/**
 * 2023-02-06
 * pureum
 */
class Adapter(

) :RecyclerView.Adapter<Adapter.MyAdapter>(){

    lateinit var onSaveClic : (DomainRoomData)->Unit

    private lateinit var binding:ListBinding
    var dataList = listOf<DomainRoomData>()
    set(value){
        field = value
        notifyDataSetChanged()
    }


    inner class MyAdapter(private val binding: ListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list:DomainRoomData){
            val myDate = list.date.split("-","T")
            binding.cardName.text = "${list.cardName} :"
            binding.amount.text = " ${list.amount}"
            binding.date.text = "${myDate[0]}.${myDate[1]}.${myDate[2]} / ${myDate[3]}"
            binding.imageView2.setImageURI(list.picture.toUri())

            binding.listLayout.setOnClickListener{
                onSaveClic(list)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        binding = ListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyAdapter(binding)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size
}