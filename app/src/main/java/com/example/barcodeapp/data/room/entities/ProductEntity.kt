package com.example.barcodeapp.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val barcodes: ArrayList<String>?,
    val code: Int,
    val measurement: String,
    val salesPrice: Double,
    val name: String,
    val description: String,
    val categoryId: String
) : Serializable