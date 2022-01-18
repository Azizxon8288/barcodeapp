package com.example.barcodeapp.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val productCount: Int,
    val name: String,
    val imageUrl: String?
) : Serializable