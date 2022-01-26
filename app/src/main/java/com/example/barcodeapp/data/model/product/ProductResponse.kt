package com.example.barcodeapp.data.model.product


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.barcodeapp.data.model.category.CategoryResponse

@Serializable
data class ProductResponse(
    @SerialName("id")
    val id: String,

    @SerialName("barcode")
    val barcode: String? = null,

    @SerialName("code")
    val code: Int? = null,

    @SerialName("measurement")
    val measurement: String? = null,

    @SerialName("salesPrice")
    val salesPrice: Double? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("imageUrl")
    val imageUrl: String? = null,

    @SerialName("category")
    val category: CategoryResponse,
)