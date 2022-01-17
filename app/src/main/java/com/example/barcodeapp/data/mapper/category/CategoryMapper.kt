package com.example.barcodeapp.data.mapper.category

import com.example.barcodeapp.data.model.category.Category
import com.example.barcodeapp.data.model.category.CategoryResponse

fun List<CategoryResponse>.mapToCategory() =
    map { it.mapToCategory() }

fun CategoryResponse.mapToCategory(): Category =
    Category(
        id = id,
        productCount = productCount,
        name = name,
        imageUrl = imageUrl
    )