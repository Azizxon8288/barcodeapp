package com.example.barcodeapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.barcodeapp.data.model.GithubUser
import com.example.barcodeapp.data.model.category.Category
import com.example.barcodeapp.data.room.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = REPLACE)
    suspend fun addAll(categoryList: List<CategoryEntity>)

    @Query("select * from categoryentity")
    suspend fun getAll(): List<CategoryEntity>
}