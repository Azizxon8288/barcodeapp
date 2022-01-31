package com.example.barcodeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodeapp.R
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.databinding.ItemCategoryBinding
import com.squareup.picasso.Picasso

class CategoryAdapter(var list: List<CategoryEntity>, var listener: OnItemClick) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(var itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun onBind(category: CategoryEntity) {
            itemCategoryBinding.apply {
                tv.text = category.name
                Picasso.get().load(category.imageUrl).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background).into(image)
            }
            itemView.setOnClickListener {
                listener.onItemClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size


    interface OnItemClick {
        fun onItemClick(category: CategoryEntity)
    }

}