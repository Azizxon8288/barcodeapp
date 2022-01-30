package com.example.barcodeapp.data.mapper.product

import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.model.product.Product
import com.example.barcodeapp.data.model.product.ProductResponse

fun List<ProductResponse>.map() =
    map { it.mapToProduct() }

fun ProductResponse.mapToProduct() =
    Product(
        id = id,
        barcodes = barcodes ?: emptyList(),
        code = code ?: 0,
        measurement = measurement ?: "",
        salesPrice = salesPrice ?: 0.0,
        name = name ?: id,
        description = description ?: "",
        category = category.mapToCategory()
    )