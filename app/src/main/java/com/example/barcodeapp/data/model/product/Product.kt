package com.example.barcodeapp.data.model.product


import com.example.barcodeapp.data.model.category.Category

data class Product(
    val id: String,
    val barcode: String,
    val code: Int,
    val measurement: String,
    val salesPrice: Double,
    val name: String,
    val description: String,
    val category: Category,
)