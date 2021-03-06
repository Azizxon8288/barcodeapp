package com.example.barcodeapp.data.model.category

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Category(
    val id: String,
    val productCount: Int,
    val name: String,
    val imageUrl: String?
)