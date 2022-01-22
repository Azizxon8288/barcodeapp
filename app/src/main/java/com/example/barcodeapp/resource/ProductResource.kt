package com.example.barcodeapp.resource

import com.example.barcodeapp.data.room.entities.ProductEntity

sealed class ProductResource {
    object Loading : ProductResource()

    data class Error(val message: String?) : ProductResource()

    data class Success(val list: List<ProductEntity>) : ProductResource()
}
