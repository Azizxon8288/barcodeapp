package com.example.barcodeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodeapp.data.model.product.Data
import com.example.barcodeapp.databinding.ItemListBinding

class PagerAdapter : PagingDataAdapter<Data, PagerAdapter.MyPagingHolder>(MyDiffUtil()) {



    class MyDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyPagingHolder(private val itemListBinding: ItemListBinding) :
        RecyclerView.ViewHolder(itemListBinding.root) {
        fun onBind(productEntity: Data?) {
            itemListBinding.tv.text = productEntity?.name
        }
    }

    override fun onBindViewHolder(holder: MyPagingHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPagingHolder {
        return MyPagingHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
}