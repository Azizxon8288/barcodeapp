package com.example.barcodeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodeapp.databinding.ItemListBinding
import com.example.barcodeapp.models.Lists

class ListAdapter(private var list: List<Lists>, var listener: OnItemClickListener) :
    RecyclerView.Adapter<ListAdapter.MyHolder>() {
    inner class MyHolder(var itemListBinding: ItemListBinding) :
        RecyclerView.ViewHolder(itemListBinding.root) {
        fun onBind(lists: Lists) {
            itemListBinding.apply {
                image.setImageResource(lists.image)
                tv.text = lists.name
                card1.setOnClickListener {
                    listener.onItemClick(lists)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(lists: Lists)
    }
}