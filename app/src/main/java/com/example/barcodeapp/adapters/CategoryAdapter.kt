//package com.example.barcodeapp.adapters
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.barcodeapp.databinding.ItemCategoryBinding
//import com.example.barcodeapp.models.Category
//
//class CategoryAdapter(var list: List<Category>, var listener:OnItemClick) :
//    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
//    inner class MyViewHolder(var itemCategoryBinding: ItemCategoryBinding) :
//        RecyclerView.ViewHolder(itemCategoryBinding.root) {
//        fun onBind(category: Category) {
//            itemCategoryBinding.apply {
//                tv.text = category.name
//                image.setImageResource(category.image)
//            }
//            itemView.setOnClickListener {
//                listener.onItemClick(category)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder(
//            ItemCategoryBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.onBind(list[position])
//    }
//
//    override fun getItemCount(): Int = list.size
//
//
//    interface OnItemClick {
//        fun onItemClick(category: Category)
//    }
//
//}