package com.example.barcodeapp.data.model.product

import com.example.barcodeapp.data.model.category.CategoryResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

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

    @SerialName("categoryDTO")
    val category: CategoryResponse,
)