package com.example.barcodeapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.barcodeapp.data.room.entities.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = REPLACE)
    suspend fun addList(productList: List<ProductEntity>)

    @Query("select * from productentity")
    suspend fun getAllProduct(): List<ProductEntity>

//    @Query("select * from productentity where code like :searchQuery or code like :searchQuery")
//    suspend fun productByCodeSearch(searchQuery: String): List<ProductEntity>
//
//    @Query("select * from productentity where barcode like :str or barcode like :str")
//    suspend fun productByBarCodeSearch(str: String): List<ProductEntity>

    @Query("select * from productentity where barcode = :barcode")
    suspend fun searchByBarCode(barcode: String?): ProductEntity

    @Query("select * from productentity where barcode = :barcode")
    suspend fun searchByBarCodeBoolean(barcode: String?): Boolean
}