package com.example.barcodeapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.barcodeapp.data.model.product.Product
import com.example.barcodeapp.data.room.entities.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = REPLACE)
    fun addList(productList: List<ProductEntity>)

    @Query("select * from productentity")
    fun getAllProduct(): List<ProductEntity>

    @Query("select * from productentity where code like :searchQuery or code like :searchQuery")
    fun productByCodeSearch(searchQuery: String): List<ProductEntity>

    @Query("select * from productentity where barcode like :str or barcode like :str")
    fun productByBarCodeSearch(str: String): List<ProductEntity>

    @Query("select * from productentity where barcode = :barcode")
    fun searchByBarCode(barcode: String?): ProductEntity

    @Query("select * from productentity where barcode = :barcode")
    fun searchByBarCodeBoolean(barcode: String?): Boolean
}