package com.example.barcodeapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.barcodeapp.data.room.entities.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = REPLACE)
    suspend fun addAll(productList: List<ProductEntity>)

    @Query("select * from productentity")
    suspend fun getAll(): List<ProductEntity>

    @Query("select * from productentity where barcode = :barcode")
    suspend fun searchByBarCode(barcode: String): ProductEntity?
}