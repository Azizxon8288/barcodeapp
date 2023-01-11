package com.example.barcodeapp.data.model.product

data class Data(
    val barcodes: List<String>,
    val categoryId: String,
    val code: Int,
    val description: String,
    val id: String,
    val imageUrl: String,
    val measurement: String,
    val name: String,
    val salesPrice: Double
)