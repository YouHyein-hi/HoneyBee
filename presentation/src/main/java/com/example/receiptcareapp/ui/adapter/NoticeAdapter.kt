package com.example.receiptcareapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.DomainGetNoticeListData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.ItemNoticeBinding

/**
 * 2023-08-13
 * pureum
 */
class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.MyHolder>(){

    private lateinit var binding: ItemNoticeBinding
    var dataList = mutableListOf<DomainGetNoticeListData>()

    inner class MyHolder(private val binding : ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainGetNoticeListData) {
            binding.noticeItemTitle.text = item.title
            binding.noticeItemDate.text = item.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) { holder.bind(dataList[position]) }

    override fun getItemCount(): Int = dataList.size
}