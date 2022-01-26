package com.example.barcodeapp.data.mapper.product

import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.model.product.Product
import com.example.barcodeapp.data.model.product.ProductEntity
import com.example.barcodeapp.data.model.product.ProductResponse

fun List<ProductResponse>.map() =
    map { it.mapToProduct() }

//fun ProductResponse.mapToEntity() =
//    ProductEntity(
//        id = id,
//        categoryId = category.id,
//        barcode = barcode ?: "",
//        code = code ?: 0,
//        measurement = measurement ?: "",
//        salesPrice = salesPrice ?: 0.0,
//        name = name ?: id,
//        description = description ?: "",
//        imageUrl = imageUrl ?: "",
//        category = category.mapToCategory()
//    )

fun ProductResponse.mapToProduct() =
    ProductEntity(
        id = id,
        categoryId = category.id,
        barcode = barcode ?: "",
        code = code ?: 0,
        measurement = measurement ?: "",
        salesPrice = salesPrice,
        name = name ?: id,
        description = description,
        imageUrl = imageUrl
    )