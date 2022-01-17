package com.example.barcodeapp.data.model.category

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CategoryResponse(
    @SerialName("id")
    val id: String,

    @SerialName("productCount")
    val productCount: Int = 0,

    @SerialName("name")
    val name: String,

    @SerialName("imageUrl")
    val imageUrl: String? = null
)