package com.example.barcodeapp.resource

import com.example.barcodeapp.data.room.entities.ProductEntity

sealed class SearchResource {
    object Loading : SearchResource()

    data class Error(val message: String) : SearchResource()

    data class Success(val productEntity: ProductEntity) : SearchResource()
}
