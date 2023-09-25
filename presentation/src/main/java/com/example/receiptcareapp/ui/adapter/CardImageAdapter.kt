package com.example.receiptcareapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptcareapp.databinding.ItemCardImageBinding

class CardImageAdapter(private val image: List<Int>) :
    RecyclerView.Adapter<CardImageAdapter.MyHolder>() {

    lateinit var onCardImageClick: (position: Int) -> Unit
    private lateinit var binding: ItemCardImageBinding

    inner class MyHolder(private val binding: ItemCardImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int, position: Int) {
            binding.itemCardItem.setImageResource(image)
            binding.root.setOnClickListener {
                onCardImageClick(position + 1)
            }
            binding.position = position+1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardImageAdapter.MyHolder {
        binding = ItemCardImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: CardImageAdapter.MyHolder, position: Int) {
        val imageItem = image[position]
        holder.bind(imageItem, position)
    }

    override fun getItemCount(): Int {
        return image.size
    }

}