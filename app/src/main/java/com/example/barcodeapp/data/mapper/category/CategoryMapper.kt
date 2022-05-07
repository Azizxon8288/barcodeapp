package com.example.barcodeapp.data.mapper.category

import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.room.entities.CategoryEntity

fun List<CategoryResponse>.mapToCategory() =
    map { it.mapToCategory() }

fun CategoryResponse.mapToCategory(): CategoryEntity =
    CategoryEntity(
        id = id,
        productCount = productCount,
        name = name,
        imageUrl = imageUrl
    )