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
                //TODO 데이터 클래스로 묶어서 XML로 데이터 바인딩 연결
                recordItemStoreTxt.text = list.storeName
                recordItemCardTxt.text = "${list.cardName}카드"
                recordItemAmountTxt.text = list.storeAmount
                recordItemDateTxt.text = list.date
                listLayout.setOnClickListener{ onServerSaveClick(list) }

                //TODO 데이터 클래스로 묶어서 XML로 데이터 바인딩 연결
                //TODO visiblity 는 databinding 으로 뺼수있을것같은데 bringToFont는 못뺄듯
                if(list.billCheck) {
                    billCheckLayout.visibility = View.VISIBLE
                    billCheckLayout.bringToFront()
                }
                else billCheckLayout.visibility = View.INVISIBLE
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