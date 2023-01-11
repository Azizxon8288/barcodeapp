package com.example.barcodeapp.data.model.product

data class ProductPagerResponse(
    val data: List<Data>,
    val limit: String,
    val page: String
)