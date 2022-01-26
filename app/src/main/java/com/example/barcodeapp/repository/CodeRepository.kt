package com.example.barcodeapp.repository

import com.example.barcodeapp.data.model.GithubUser
import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.model.product.ProductResponse
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.Webservice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CodeRepository(val appDatabase: AppDatabase, val webservice: Webservice) {

    // Product

    suspend fun getAllProduct(): Flow<Response<List<ProductResponse>>> {
        return flow { emit(webservice.getAllProducts()) }
    }

    suspend fun addDbProducts(list: List<ProductEntity>) {
        appDatabase.productDao().addAll(list)
    }

    suspend fun getDBProducts() = flow { emit(appDatabase.productDao().getAll()) }


    // shunchaki

    suspend fun getUsers(): Flow<Response<List<GithubUser>>> {
        return flow { emit(webservice.getUsers()) }
    }


    //  Search

    suspend fun searchByBarCode(barcode: String): Flow<ProductEntity?> {
        return flow { emit(appDatabase.productDao().searchByBarCode(barcode)) }
    }


    // Category


    suspend fun addDbCategories(list: List<CategoryEntity>) {
        appDatabase.categoryDao().addAll(list)
    }

    suspend fun getDBCategories() = flow { emit(appDatabase.categoryDao().getAll()) }


    suspend fun getAllCategory(): Flow<Response<List<CategoryResponse>>> {
        return flow { emit(webservice.getAllCategories()) }
    }
}