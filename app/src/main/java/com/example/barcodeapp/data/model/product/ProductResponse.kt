package com.example.barcodeapp.data.model.product


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("id")
    val id: String,

    @SerialName("barcodes")
    val barcodes: List<String>? = null,

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

    @SerialName("categoryId")
    val categoryId: String,
)