package com.example.barcodeapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.barcodeapp.data.model.category.Category
import com.example.barcodeapp.data.room.entities.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = REPLACE)
    fun addList(categoryList: List<CategoryEntity>)

    @Query("select * from categoryentity")
    fun getAllCategory(): List<CategoryEntity>

}