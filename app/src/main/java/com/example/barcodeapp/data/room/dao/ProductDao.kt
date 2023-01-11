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
    fun getAllProduct(): List<ProductEntity>

    @Query("select * from productentity where categoryId =:categoryId")
    fun getProductsByCategoryId(categoryId: String): List<ProductEntity>

    // main search shu
    @Query("select * from productentity where name like :searchQuery or code like :searchQuery")
    fun productByCodeSearch(searchQuery: String): List<ProductEntity>

    @Query("select * from productentity where id =:productId")
    suspend fun getProductByProductId(productId: String): ProductEntity

    @Query("SELECT * FROM productentity WHERE barcodes LIKE '%' || :barcode || '%'")
    fun searchByBarCode(barcode: String?): ProductEntity?

//    @Query("Delete from productentity")
//    fun deleteAll()
}