package com.example.barcodeapp.data.mapper.product

import com.example.barcodeapp.data.model.product.Data
import com.example.barcodeapp.data.model.product.ProductResponse
import com.example.barcodeapp.data.room.entities.ProductEntity

fun List<ProductResponse>.mapToProductList() =
    map { it.mapToProduct() }

fun ProductResponse.mapToProduct() =
    ProductEntity(
        id = id,
        barcodes = barcodes ?: emptyList(),
        code = code ?: 0,
        measurement = measurement ?: "",
        salesPrice = salesPrice ?: 0.0,
        name = name ?: id,
        description = description ?: "",
        imageUrl = imageUrl ?: "",
        categoryId = categoryId
    )

fun List<Data>.maptoProductList() =
    map { it.mapToProduct() }


fun Data.mapToProduct() =
    ProductEntity(
        id = id,
        barcodes = barcodes ?: emptyList(),
        code = code ?: 0,
        measurement = measurement ?: "",
        salesPrice = salesPrice,
        name = name ?: id,
        description = description ?: "",
        imageUrl = imageUrl ?: "",
        categoryId = categoryId
    )
