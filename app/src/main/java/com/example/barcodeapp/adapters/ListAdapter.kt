package com.example.barcodeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodeapp.R
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.databinding.ItemListBinding
import com.squareup.picasso.Picasso

class ListAdapter(var list: List<ProductEntity>, var listener: OnItemClickListener) :
    RecyclerView.Adapter<ListAdapter.MyHolder>() {
    inner class MyHolder(var itemListBinding: ItemListBinding) :
        RecyclerView.ViewHolder(itemListBinding.root) {
        fun onBind(product: ProductEntity) {
            itemListBinding.apply {
                Picasso.get().load(product.imageUrl).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background).into(image)
                tv.text = product.name
                card1.setOnClickListener {
                    listener.onItemClick(product)
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
        fun onItemClick(product: ProductEntity)
    }
}