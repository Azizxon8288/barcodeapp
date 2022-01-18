package com.example.barcodeapp.resource

import com.example.barcodeapp.data.room.entities.CategoryEntity

sealed class CategoryResource {
    object Loading : CategoryResource()

    data class Error(val message: String) : CategoryResource()

    data class Success(val list: List<CategoryEntity>) : CategoryResource()
}
