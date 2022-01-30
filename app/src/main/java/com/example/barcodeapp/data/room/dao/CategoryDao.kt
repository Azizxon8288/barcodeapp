package com.example.barcodeapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.barcodeapp.data.room.entities.CategoryEntity

@Dao
interface CategoryDao {


//    @Query("SELECT * FROM person_table WHERE firstName LIKE :searchQuery OR lastName LIKE :searchQuery") // search ikkitali

    @Insert(onConflict = REPLACE)
    suspend fun addList(categoryList: List<CategoryEntity>)

    @Query("select * from categoryentity")
    suspend fun getAllCategory(): List<CategoryEntity>
}