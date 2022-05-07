package com.example.barcodeapp.data.model.product


import com.example.barcodeapp.data.room.entities.CategoryEntity

data class Product(
    val id: String,
    val barcodes: List<String>,
    val code: Int,
    val measurement: String,
    val salesPrice: Double,
    val name: String,
    val description: String,
    val category: CategoryEntity,
)