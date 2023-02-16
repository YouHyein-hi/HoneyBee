package com.example.receiptcareapp.fragment.recyclerFragment

import android.content.Entity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.DomainRoomData
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import kotlinx.coroutines.NonDisposableHandle.parent

/**
 * 2023-02-06
 * pureum
 */
class Adapter(

) :RecyclerView.Adapter<Adapter.MyAdapter>(){

    private lateinit var binding:FragmentRecyclerBinding
    var dataList = listOf<DomainRoomData>()
    set(value){
        field = value
        notifyDataSetChanged()
        Log.e("TAG", "$dataList: ", )
    }


    inner class MyAdapter():RecyclerView.ViewHolder(binding.root){
        fun bind(list:DomainRoomData){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}