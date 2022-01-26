package com.example.barcodeapp.data.model.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import java.math.BigDecimal

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val id: String,

    @ColumnInfo(name = "product_category_id")
    val categoryId: String,

    @ColumnInfo(name = "product_barcode")
    val barcode: String,

    @ColumnInfo(name = "product_code")
    val code: Int?,

    @ColumnInfo(name = "product_measurement")
    val measurement: String?,

    @ColumnInfo(name = "product_sales_price")
    val salesPrice: Double?,

    @ColumnInfo(name = "product_name")
    val name: String,

    @ColumnInfo(name = "product_description")
    val description: String?,

    @ColumnInfo(name = "product_image_url")
    val imageUrl: String?,
)